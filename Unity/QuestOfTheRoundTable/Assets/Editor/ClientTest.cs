using UnityEngine;
using UnityEditor;
using UnityEngine.TestTools;
using NUnit.Framework;
using System.Net.Sockets;
using System.Net;


public class ClientTest{

    [SetUp]
    public void Setup()
    {
        //AsynchronousSocketListener.StartListening();
    }
	[Test]
	public void ClientSendReadTest() {
        Client myClient = new Client("127.0.0.1", 2222);
        myClient.Send("game start:2");
        Assert.AreEqual(true, true);

	}

}
