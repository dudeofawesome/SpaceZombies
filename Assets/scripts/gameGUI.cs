using UnityEngine;
using System.Collections;
using GooglePlayGames;
using UnityEngine.SocialPlatforms;
using Holoville.HOTween;

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
	[SerializeField] GUISkin plainLabel;

	// Use this for initialization
	void Start () {
		gameEngine = GameObject.Find("GameEngine").GetComponent<GameEngine>();

		scale.x = Screen.width / originalWidth; // calculate hor scale
		scale.y = Screen.height / originalHeight; // calculate vert scale
		scale.z = 1;
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown(KeyCode.Escape) && !GameEngine.gameOver) {
			if (Time.timeScale == 1) {
				Time.timeScale = 0;
				HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 1, new TweenParms().Prop("position", new Vector3(-1.77f, -1.98f, -10f)).UpdateType(UpdateType.TimeScaleIndependentUpdate));
				HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 1, new TweenParms().Prop("localRotation", Quaternion.Euler(346.5201f, 8.390002f, 0f)).UpdateType(UpdateType.TimeScaleIndependentUpdate));
				HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.camera, 1, new TweenParms().Prop("orthographicSize", 3.8f).UpdateType(UpdateType.TimeScaleIndependentUpdate));
			}
			else {
				// Time.timeScale = 1;
				HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 1, new TweenParms().Prop("position", new Vector3(0, 0, -10)).UpdateType(UpdateType.TimeScaleIndependentUpdate));
				HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 1, new TweenParms().Prop("localRotation", Quaternion.Euler(0, 0, 0)).UpdateType(UpdateType.TimeScaleIndependentUpdate));
				HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.camera, 1, new TweenParms().Prop("orthographicSize", 5).UpdateType(UpdateType.TimeScaleIndependentUpdate).OnComplete(completePause));
			}
		}
	}

	void completePause () {
		Time.timeScale = 1;
	}

	void OnGUI () {
		// GUI.color.a = 0.8f;\
		Color _c = new Color(1f,1f,1f,0.8f);
		GUI.color = _c;
		GUI.skin = plainLabel;
		GUI.Label(new Rect(5, 5, Screen.width, 50), messages);

		var svMat = GUI.matrix; // save current matrix
		GUI.matrix = Matrix4x4.TRS (Vector3.zero, Quaternion.identity, scale);

		GUI.Label(new Rect(originalWidth - 60, originalHeight - 30, 70, 25), GameEngine.totalScore + "");

		GUI.skin = guiSkin;
		if (GameEngine.showGUI || Time.timeScale == 0) {
			switch (menuPosition) {
				case MenuState.MAIN:
					int _width = 200;
					int _height = 34;
					int _startPosition = (int) (originalHeight / 2) - 120;
					if (GUI.Button (new Rect (0, _startPosition, _width, _height), "Start")) {
						if (GameEngine.gameOver)
							gameEngine.GetComponent<GameEngine>().reset();
						else {
							HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 1, new TweenParms().Prop("position", new Vector3(0, 0, -10)).UpdateType(UpdateType.TimeScaleIndependentUpdate));
							HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 1, new TweenParms().Prop("localRotation", Quaternion.Euler(0, 0, 0)).UpdateType(UpdateType.TimeScaleIndependentUpdate));
							HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.camera, 1, new TweenParms().Prop("orthographicSize", 5).UpdateType(UpdateType.TimeScaleIndependentUpdate).OnComplete(completePause));
						}
					}
					if (GUI.Button (new Rect (0, _startPosition + _height, _width, _height), "Calibrate")) {
						calibratedRotation = new Vector2(Input.acceleration.x, Input.acceleration.y);
					}

					GUI.Label(new Rect (0, _startPosition + (_height * 2), _width, _height), "Max # of particles");
					PlayerPrefs.SetInt ("maxParticles", (int) GUI.HorizontalSlider(new Rect (0, _startPosition + (_height * 2) + 2, _width - 5, _height), PlayerPrefs.GetInt ("maxParticles"), 50, 2000));
					GameEngine.maxParticleCount = PlayerPrefs.GetInt ("maxParticles");

					if (GUI.Button (new Rect (0, _startPosition + (_height * 3), _width, _height), "Leaderboards")) {
						// Social.ShowLeaderboardUI();
						((PlayGamesPlatform) Social.Active).ShowLeaderboardUI("CgkIgpP17bANEAIQCQ");
					}
					if (GUI.Button (new Rect (0, _startPosition + (_height * 4), _width, _height), "Achievements")) {
						Social.ShowAchievementsUI();
					}
					// if (GUI.Button (new Rect (0, _startPosition + (_height * 5), _width, _height), "Support the Dev")) {
					// 	if (GameEngine.gameOver)
					// 		gameEngine.GetComponent<GameEngine>().reset();
					// 	else {
					// 		Time.timeScale = 1;
					// 	}
					// }

					GUI.skin = plainLabel;
					GUI.Label(new Rect(originalWidth - 105, originalHeight - 30, 45, 25), "Score: ");
					GUI.Label(new Rect(originalWidth - 205, originalHeight - 50, 150, 25), "Total Zombies Killed: ");
					GUI.Label(new Rect(originalWidth - 60, originalHeight - 50, 45, 25), GameEngine.totalZombiesKilled + "");
				break;
			}
		}
		GUI.matrix = svMat; // restore matrix
	}
}
