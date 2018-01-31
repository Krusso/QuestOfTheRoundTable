using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Net.Sockets;
using System.IO;
using System;
using System.Text;
public class Client{
    private string serverIp = "127.0.0.1";
    private int port = 2223;

    NetworkStream stream;
    StreamWriter writer;
    StreamReader reader;
    Queue<string> messageQueue;

    public Client()
    {

    }
    public Client(string serverIp, int port)
    {
        TcpClient client = new TcpClient(serverIp, port);
        Debug.Log("New Client created type, connected to: " + serverIp +":"+ port);
        stream = client.GetStream();
        stream.ReadTimeout = 10;
        if (stream.CanRead)
        {
            reader = new StreamReader(stream);
            Debug.Log("StreamReader created");
        }
        if (stream.CanWrite)
        {
            writer = new StreamWriter(stream);
            Debug.Log("StreamWriter created");
        }
        messageQueue = new Queue<string>();
    }
    public void Send(string data)
    {
        Debug.Log("Sending msg:" + data);
        writer.WriteLine(data);
        writer.Flush();
    }

    public void ReadData()
    {
        if (stream.DataAvailable)
        {
            string data = reader.ReadLine();
            Debug.Log("Message received:" + data);
            messageQueue.Enqueue(data);
        }
    }

    public string GetMessage()
    {
        if (messageQueue.Count != 0)
        {
            return messageQueue.Dequeue();
        }
        return null;
    }
}
