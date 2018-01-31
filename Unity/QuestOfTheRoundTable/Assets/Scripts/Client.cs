using System.Collections;
using System.Collections.Generic;
using UnityEngine.Networking;
using UnityEngine;
using System.Net.Sockets;
using System.IO;
using System;
using System.Text;

public class Client{
    private bool isAtStartup = true;
    const string serverIp = "127.0.0.1";
    const int port = 2223;

    NetworkStream stream;
    StreamWriter writer;
    public Client()
    {

    }
    public Client(string serverIp, int port)
    {
        TcpClient client = new TcpClient(serverIp, port);
        Debug.Log("New Client greated type: " + client.GetType());
        stream = client.GetStream();

        stream.ReadTimeout = 10;
        if (stream.CanRead)
        {
            writer = new StreamWriter(stream);
            Debug.Log("Writer created");
        }
    }
    public void send(string data)
    {
        Debug.Log("Sending message:  " + data);
        writer.Write(data);
        writer.Flush();
    }

    void readData()
    {
        if (stream.CanRead)
        {
            try
            {
                byte[] buff = new byte[1024];
                StringBuilder realMessage = new StringBuilder();
                int numBytesRead = 0;
                while (stream.DataAvailable){
                    numBytesRead = stream.Read(buff, 0, buff.Length);
                    realMessage.AppendFormat("{0}", Encoding.ASCII.GetString(buff, 0, numBytesRead));
                }
                Debug.Log("Message Received: " + realMessage);
            }catch(Exception e)
            {
                Debug.Log("Read data failed: " + e.ToString());
            }
        }
    }
    void performTask(string message)
    {
        switch(message)
        {
            
        }
    }
}
