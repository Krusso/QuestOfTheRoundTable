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
    
    private void Awake()
    {
    }
    void Start()
    {
        NumPlayersField = GameObject.Find("NumPlayersField");
       
    }
	
	// Update is called once per frame
	void Update ()
    {
        Debug.Log("playerfieldText: " + playerfieldText.text);
        
    }
    public void StartGame()
    {


    }
}
