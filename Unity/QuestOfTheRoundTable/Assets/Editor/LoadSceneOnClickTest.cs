using UnityEngine;
using UnityEditor;
using UnityEngine.TestTools;
using NUnit.Framework;
using System.Collections;

public class LoadSceneOnClickTest : IPrebuildSetup {
    [SetUp]
    public void Setup()
    {
        LoadSceneOnClick.print("Setting Up");
    }
	[Test]
	public void LoadSceneOnClickTestSimplePasses() {
        LoadSceneOnClick.print("Testing");
        
        Assert.AreEqual(true, true);
	}

/*	// A UnityTest behaves like a coroutine in PlayMode
	// and allows you to yield null to skip a frame in EditMode
	[UnityTest]
	public IEnumerator LoadSceneOnClickTestWithEnumeratorPasses() {
		// Use the Assert class to test conditions.
		// yield to skip a frame
		yield return null;
	}
    */
}
