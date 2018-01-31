using UnityEngine;
using UnityEngine.TestTools;
using NUnit.Framework;
using System.Collections;
using UnityEngine.UI;

public class TitleMenuButton {


	// A UnityTest behaves like a coroutine in PlayMode
	// and allows you to yield null to skip a frame in EditMode
	[UnityTest]
	public IEnumerator StartButtonTest() {

        SetupScene();

        Button StartButton = GameObject.Find("StartButton").GetComponent<Button>();

        Button HelpButton = GameObject.Find("HelpButton").GetComponent<Button>();
        Button SBackButton = GameObject.Find("StartButton").GetComponent<Button>();
        Button AudioButton = GameObject.Find("AudioButton").GetComponent<Button>();
        StartButton.onClick.Invoke();
        if (GameObject.Find("StartButton").activeSelf == true)
        {
            Assert.Fail();
        }
        yield return new WaitForSeconds(1);
    }
    void SetupScene()
    {
        //MonoBehaviour.Instantiate(Resources.Load<GameObject>("SF Scene Elements"));
         MonoBehaviour.Instantiate(Resources.Load("Prefabs/TitleMenu"));
    }
}
