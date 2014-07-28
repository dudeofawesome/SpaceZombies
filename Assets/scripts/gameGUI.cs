using UnityEngine;
using System.Collections;
using GooglePlayGames;
using UnityEngine.SocialPlatforms;

public class gameGUI : MonoBehaviour {
	public enum MenuState {
		MAIN,
		PAUSE,
		CONTROLSETTINGS,
		VISUALEFFECTS
	}
	public MenuState menuPosition = MenuState.MAIN;

	public static string messages = "";
	private float originalWidth = 320;
	private float originalHeight = 640;
	private Vector3 scale;
	public static Vector2 calibratedRotation = new Vector2(0,0);

	[SerializeField] GameEngine gameEngine = null;
	[SerializeField] GUISkin guiSkin;

	// Use this for initialization
	void Start () {
		gameEngine = GameObject.Find("GameEngine").GetComponent<GameEngine>();

		scale.x = Screen.width / originalWidth; // calculate hor scale
		scale.y = Screen.height / originalHeight; // calculate vert scale
		scale.z = 1;
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown(KeyCode.Escape)) {
			if (Time.timeScale == 1)
				Time.timeScale = 0;
			else
				Time.timeScale = 1;
		}
	}

	void OnGUI () {
		GUI.skin = guiSkin;
		GUI.Label(new Rect(5, 5, Screen.width, 50), messages);

		var svMat = GUI.matrix; // save current matrix
		GUI.matrix = Matrix4x4.TRS (Vector3.zero, Quaternion.identity, scale);

		GUI.Label(new Rect(originalWidth - 50, originalHeight - 30, 45, 25), GameEngine.totalScore + "");

		if (GameEngine.gameOver || Time.timeScale == 0) {
			switch (menuPosition) {
				case MenuState.MAIN:
					int _width = 200;
					if (GUI.Button (new Rect (0, originalHeight / 2 - 96, _width, 24), "Start")) {
						if (GameEngine.gameOver)
							gameEngine.GetComponent<GameEngine>().reset();
						GameEngine.gameOver = false;
						Time.timeScale = 1;
					}
					if (GUI.Button (new Rect (0, originalHeight / 2 - 72, _width, 24), "Calibrate")) {
						calibratedRotation = new Vector2(Input.acceleration.x, Input.acceleration.y);
					}

					GUI.Label(new Rect (0, originalHeight / 2 - 45, _width, 24), "Max # of particles");
					PlayerPrefs.SetInt ("maxParticles", (int) GUI.HorizontalSlider(new Rect (0, originalHeight / 2 - 24, _width, 24), PlayerPrefs.GetInt ("maxParticles"), 50, 2000));
					GameEngine.maxParticleCount = PlayerPrefs.GetInt ("maxParticles");

					if (GUI.Button (new Rect (0, originalHeight / 2, _width, 24), "Leaderboards")) {
						// Social.ShowLeaderboardUI();
						((PlayGamesPlatform) Social.Active).ShowLeaderboardUI("CgkIgpP17bANEAIQCQ");
					}
					if (GUI.Button (new Rect (0, originalHeight / 2 + 24, _width, 24), "Achievements")) {
						Social.ShowAchievementsUI();
					}
					if (GUI.Button (new Rect (0, originalHeight / 2 + 48, _width, 24), "Support the Dev")) {
						if (GameEngine.gameOver)
							gameEngine.GetComponent<GameEngine>().reset();
						GameEngine.gameOver = false;
						Time.timeScale = 1;
					}

					GUI.Label(new Rect(originalWidth - 50, originalHeight - 50, 45, 25), GameEngine.totalZombiesKilled + "");
				break;
			}
		}
		GUI.matrix = svMat; // restore matrix
	}
}
