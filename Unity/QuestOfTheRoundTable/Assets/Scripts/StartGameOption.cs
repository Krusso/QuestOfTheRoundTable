using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System.Linq;
using UnityEngine.SceneManagement;

public class StartGameOption : MonoBehaviour {

    private GameObject NumPlayersField;
    private Text playerfieldText;
    private const int gameScene = 1;
    public Game game;
    
    private void Awake()
    {
        DontDestroyOnLoad(game);
    }
    void Start()
    {
        NumPlayersField = GameObject.Find("NumPlayersField");
    }
	
	// Update is called once per frame
	void Update ()
    {      
    }
    public void StartGame()
    {
        GameObject playersfieldtext = GameObject.Find("/Canvas/PlayerOptionPanel/NumPlayersField/Text");
        Debug.Log("playersfieldtext: " + playersfieldtext.ToString());
        Text txt = playersfieldtext.GetComponent<Text>();
        Debug.Log("txt: " + txt.text);
        game = new Game();
        game.NewGame(int.Parse(txt.text));

    }

    
}
