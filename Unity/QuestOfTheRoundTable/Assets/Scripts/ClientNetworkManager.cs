using System.Collections;
using System.Collections.Generic;
using UnityEngine.Networking;
using UnityEngine;

public class ClientNetworkManager : MonoBehaviour {
    public bool isAtStartup = true;
    NetworkClient client;
    const string serverIp = "127.0.0.1";
    const int port = 2223;

    // Use this for initialization
    void Start () {
        SetupClient();
    }
	
	// Update is called once per frame
	void Update () {
        
	}

    public void SetupClient()
    {
        client = new NetworkClient();
        Debug.Log("Registering client connection");
        client.RegisterHandler(MsgType.Connect, OnConnected);
        client.RegisterHandler(MsgType.Error, OnError);
        client.Connect(serverIp, port);
        
        isAtStartup = false;
    }

    public void OnConnected(NetworkMessage netMsg)
    {
        Debug.Log("Client connected to server");        
    }

    public void OnError(NetworkMessage netMsg)
    {
        
        Debug.Log("Error occurend when connectin");
    }

}
