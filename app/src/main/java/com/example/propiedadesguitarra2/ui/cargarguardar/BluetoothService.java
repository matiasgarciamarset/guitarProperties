/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.propiedadesguitarra2.ui.cargarguardar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {

    private BluetoothAdapter mAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    private int mState;
    private int mNewState;

    private BiConsumer<byte[], Integer> onReceiveMethod;
    private Handler onConnectedMethod;

    public static int CONNECTION_STATUS = 0;

    public BluetoothService(Context context) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mNewState = mState;
    }

    public synchronized void connect(BluetoothDevice device, int buffer_size) {
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, buffer_size);
        mConnectThread.start();
        publicStatusChange("device", device.getName());
    }

    private void publicStatusChange(String name, String value) {
        if (onConnectedMethod!= null) {
            Message msg = onConnectedMethod.obtainMessage(CONNECTION_STATUS);
            Bundle bundle = new Bundle();
            bundle.putString(name, value);
            msg.setData(bundle);
            onConnectedMethod.sendMessage(msg);
            onConnectedMethod.obtainMessage(CONNECTION_STATUS, mState, -1).sendToTarget();
        }
    }

    public synchronized int getState() {
        return mState;
    }

    public void onConnectionStatusChange(Handler method) {
        this.onConnectedMethod = method;
    }

    public void onReceive(BiConsumer<byte[], Integer> method) {
        if (onReceiveMethod!=null) this.onReceiveMethod = method;
    }

    public Boolean write(byte[] out) {
        try {
            // Create temporary object
            ConnectedThread r;
            // Synchronize a copy of the ConnectedThread
            synchronized (this) {
                if (mState != 3) return false;
                r = mConnectedThread;
            }
            // Perform the write unsynchronized
            return r.write(out);
        } catch (Exception e) {
            mState = STATE_NONE;
            publicStatusChange("error", e.getMessage());
            return false;
        }
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     */
    private synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, int buffer_size) {
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, buffer_size);
        mConnectedThread.start();

        publicStatusChange("device", device.getName());
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private int buffer_size;

        public ConnectedThread(BluetoothSocket socket, int buffer_size) {
            mmSocket = socket;
            this.buffer_size = buffer_size;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                System.out.println("temp sockets not created - " + e.getMessage());
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    if (onReceiveMethod!=null) onReceiveMethod.accept(buffer, bytes);
                } catch (IOException e) {
                    System.out.println("disconnected - " + e.getMessage());
                    mState = STATE_NONE;
                    publicStatusChange("error", "Socket no disponible, pruebe reiniciando conexión en servidor");
                    break;
                }
            }
        }

        public Boolean write(byte[] buffer) {
            int size = buffer.length;
            try {
                int i = 0;
                while (i < size) {
                    int len = Math.min(buffer_size, size - i);
                    mmOutStream.write(buffer, i, len);
                    i += len;
                }
                return true;
            } catch (IOException e) {
                mState = STATE_NONE;
                publicStatusChange("error", e.getMessage());
                return false;
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                System.out.println("close() of connect socket failed - " + e.getMessage());
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final int buffer_size;

        public ConnectThread(BluetoothDevice device, int buffer_size) {
            mmDevice = device;
            this.buffer_size = buffer_size;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
                tmp = (BluetoothSocket) m.invoke(device, 1);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    System.out.println("unable to close() socket during connection failure - " + e2.getMessage());
                }
                mState = STATE_NONE;
                publicStatusChange("error", e.getMessage());
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, buffer_size);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                System.out.println("close() of connect socket failed - " + e.getMessage());
            }
        }
    }
}
