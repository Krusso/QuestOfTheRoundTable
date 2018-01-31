using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Game : MonoBehaviour {
    private const int gameScene = 1;
    public GameObject[] playerObjects;
    public Player[] players;
    Client client;
    bool inGame;
    string currentMessage;

    //Make sure we have the game manager alive during the whole duration of the application.
    void Awake()
    {
        GameObject gameManager = GameObject.Find("GameManager");
        DontDestroyOnLoad(gameManager);
    }
    // Use this for initialization
    void Start () {
        client = new Client("localhost", 2223);
        inGame = false;
        currentMessage = null;
	}
	
	// Update is called once per frame
	void Update () {
        //When we enter a game, we continuously check for new messages
        if (inGame)
        {
            client.ReadData();
            currentMessage = client.GetMessage();
            if(currentMessage != null)
            {
                Debug.Log("Game is applying message: " + currentMessage);
            }
            
        }
	}

    public void NewGame(GameObject numPlayers)
    {
        string numPlayersSelected = numPlayers.GetComponent<Text>().text;
        client.Send(Message.StartGame(numPlayersSelected));
        SceneManager.LoadScene(gameScene);
        inGame = true;
    }
}
