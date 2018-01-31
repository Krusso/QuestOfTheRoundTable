using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Game : MonoBehaviour {
    private const int gameScene = 1;
    public GameObject[] playerObjects;
    public Player[] players;
    Client client;

    //public Controller controller;

    //Make sure we have the game manager alive during the whole duration of the application.
    void Awake()
    {
        GameObject gameManager = GameObject.Find("GameManager");
        DontDestroyOnLoad(gameManager);
    }
    // Use this for initialization
    void Start () {
        client = new Client("localhost", 2223);
	}
	
	// Update is called once per frame
	void Update () {
	}

    public void NewGame(GameObject numPlayers)
    {
        string numPlayersSelected = numPlayers.GetComponent<Text>().text;
        client.send("game start:" + numPlayersSelected);
        SceneManager.LoadScene(gameScene);
    }
}
