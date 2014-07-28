using UnityEngine;
using System.Collections;
using GooglePlayGames;
using UnityEngine.SocialPlatforms;

public class GameEngine : MonoBehaviour {
	private Vector3 viewPos;
	private readonly int FREQUENCYOFNEWCHARS = 30;
	private int addNewCharacterCounter;
	public static bool gameOver = true;
	public static int totalScore = 0;
	public static int totalZombiesKilled = 0;
	public static Rect cameraViewSize;
	public static int particleCount = 0;
	public static int maxParticleCount = 200;

	[SerializeField] private GameObject prefabEnemy = null;
	[SerializeField] private GameObject prefabPlayer = null;
	[SerializeField] private GameObject player = null;
	[SerializeField] public GameObject mainCamera = null;
	[SerializeField] private Component GUIcomponent = null;

	// Use this for initialization
	void Start () {
		if (PlayerPrefs.GetInt ("maxParticles") == 0) {
			PlayerPrefs.SetInt ("maxParticles", 200);
		}
		maxParticleCount = PlayerPrefs.GetInt ("maxParticles");
		totalZombiesKilled = PlayerPrefs.GetInt ("totalZombiesKilled");

		GUIcomponent = GameObject.Find("GUI").GetComponent<gameGUI>();
		Screen.sleepTimeout = SleepTimeout.NeverSleep;

		PlayGamesPlatform.DebugLogEnabled = true;
		PlayGamesPlatform.Activate();
		Social.localUser.Authenticate((bool success) => {
			if (success)
        		gameGUI.messages += "Signed in";
        	else
        		gameGUI.messages += "Failed to sign in";
    	});

		addNewCharacterCounter = FREQUENCYOFNEWCHARS;

		// Vector3 _upLeft = mainCamera.camera.ScreenToWorldPoint(new Vector3(0, 0, Mathf.Abs(mainCamera.transform.position.z)));
		// Vector3 _downRight = mainCamera.camera.ScreenToWorldPoint(new Vector3(Screen.width, Screen.height, Mathf.Abs(mainCamera.transform.position.z)));
		// cameraViewSize = new Rect(_upLeft.x + (_upLeft.x - _downRight.x) / 2, _upLeft.y, _downRight.x + (_upLeft.x - _downRight.x) / 2, _downRight.y);
		cameraViewSize = new Rect(-10, 5, 10, -5);
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void FixedUpdate () {
		if(gameOver != true){
//			movePlayer();
//			moveEnemies();
//			testCollision();
			addNewCharacterCounter--;
			if(addNewCharacterCounter <= 0){
				addEnemy();
				addNewCharacterCounter = FREQUENCYOFNEWCHARS;
			}
		}
//		moveBullets();
//		moveParticles();
//		movePowerups();
	}

	public void addEnemy(){
		//find a place to put enemy
		int _circum = (Screen.width * 2) + (Screen.height * 2);
		int _placeOnEdge = (int) (Random.value * _circum);
		int _xLoc = 0;
		int _yLoc = 0;
		if(_placeOnEdge > Screen.width){
			if(_placeOnEdge > Screen.width + Screen.height){
				if(_placeOnEdge > Screen.width + Screen.height + Screen.width){
					_yLoc = Screen.height - (_placeOnEdge - Screen.width - Screen.height - Screen.width);
				}
				else{
					_xLoc = Screen.width - (_placeOnEdge - Screen.width - Screen.height);
					_yLoc = Screen.height;
				}
			}
			else{
				_yLoc = _placeOnEdge - Screen.width;
				_xLoc = Screen.width;
			}
		}
		else{
			_xLoc = _placeOnEdge;
		}
		
		float _diameter = (float) (Random.value * 15f + 10f) * 0.01714f;
		// int _totalV = (int) ((16 - _diameter) * 0.85);
		int _totalV = (int) ((25.1 - (_diameter / 0.01714f)) / 5);
		_totalV = (_totalV > 0) ? _totalV : 1;
		//use constructor from character.java to add in the enemy
		GameObject _enemy = (GameObject) Instantiate (prefabEnemy);
		Vector3 _pos = mainCamera.camera.ScreenToWorldPoint(new Vector3(_xLoc, _yLoc, Mathf.Abs(mainCamera.transform.position.z)));
		_enemy.transform.position = new Vector3 (_pos.x, _pos.y, 0);
		_enemy.transform.localScale = new Vector3 (_diameter, _diameter, _diameter);
		_enemy.GetComponent<moveEnemy>().velocity = _totalV;
		_enemy.GetComponent<moveEnemy>().player = player;
	}

	public void reset() {
		GameObject[] objects = GameObject.FindGameObjectsWithTag("Enemy");
		for (int i = 0; i < objects.Length; i++){
			Destroy(objects[i]);
		}
		GameObject[] objects2 = GameObject.FindGameObjectsWithTag("Powerup");
		for (int i = 0; i < objects2.Length; i++){
			Destroy(objects2[i]);
		}

		totalScore = 0;
		player = (GameObject) Instantiate(prefabPlayer);
	}
}
