using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Game : MonoBehaviour {


    private const int gameScene = 1;
    public GameObject[] playerObjects;
    public Player[] players;
    void Awake()
    {
        DontDestroyOnLoad(this);
    }
    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
        Debug.Log("I am alive");
	}

    public void NewGame(int numPlayers)
    {
        

        players = new Player[numPlayers];
        SceneManager.LoadScene(gameScene);
    }
}
