using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GooglePlayGames;
using UnityEngine.SocialPlatforms;
using Holoville.HOTween;

public class movePlayer : MonoBehaviour {
	public static int health = 100;
	public int velocity = 8;
	private int bulletThisFrame = 0;
	public List<Powerup.PowerupType> collectedPowerups = new List<Powerup.PowerupType>();
	private float diameter = 0.5f;
	public GameObject gameEngine = null;
	private int smokeCounter = 0;
	// public static Laser laser = new Laser(-10,-10,-11,-11);
	public static int twoshotLife = 200;
	public static int twoshotAlive = 0;
	public static int shotgunLife = 200;
	public static int shotgunAlive = 0;
	public static int shieldLife = 400;
	public static int shieldAlive = 0;

	[SerializeField] private GameObject prefabBullet = null;
	[SerializeField] private GameObject prefabParticle = null;

	// Use this for initialization
	void Start () {
		gameEngine = GameObject.Find("GameEngine");
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void FixedUpdate () {
		if (!GameEngine.gameOver) {
			transform.LookAt (new Vector3((Input.acceleration.x - gameGUI.calibratedRotation.x) * 1000, (Input.acceleration.y - gameGUI.calibratedRotation.y) * 1000, 0));
			transform.position = new Vector3 ((float)(transform.position.x + transform.forward.x * 0.01f * velocity), (float)(transform.position.y + transform.forward.y * 0.01f * velocity), 0);

			if (bulletThisFrame == 0) {
				shootBullet();
				bulletThisFrame = 5;
			}
			bulletThisFrame--;

			if(smokeCounter >= 4){
				GameObject _part = (GameObject) Instantiate(prefabParticle);
				_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
				_part.GetComponent<moveParticle>().changeType(moveParticle.pType.EXHAUST);
				GameEngine.particleCount++;

				if(health <= 75){
					_part = (GameObject) Instantiate(prefabParticle);
					_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
					_part.GetComponent<moveParticle>().changeType(moveParticle.pType.SMOKE);
					GameEngine.particleCount++;
				}
				if(health <= 50){
					_part = (GameObject) Instantiate(prefabParticle);
					_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
					_part.GetComponent<moveParticle>().changeType(moveParticle.pType.SMOKE);
					GameEngine.particleCount++;
					_part = (GameObject) Instantiate(prefabParticle);
					_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
					_part.GetComponent<moveParticle>().changeType(moveParticle.pType.SMOKE);
					GameEngine.particleCount++;
				}
				if(health <= 25){
					_part = (GameObject) Instantiate(prefabParticle);
					_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
					_part.GetComponent<moveParticle>().changeType(moveParticle.pType.FIRE);
					GameEngine.particleCount++;
					_part = (GameObject) Instantiate(prefabParticle);
					_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
					_part.GetComponent<moveParticle>().changeType(moveParticle.pType.FIRE);
					GameEngine.particleCount++;
				}
				smokeCounter = 0;
			}
			else{
				smokeCounter ++;
			}
		}
	}

	void OnTriggerEnter2D (Collider2D collision) {
		if (collision.gameObject.tag == "Enemy") {
			health -= 25;
			if (health <= 0) {
				endGame();
			}
		}
	}
	
	void OnCollisionEnter2D (Collision2D collision) {
		if (collision.gameObject.tag == "Enemy") {
			health -= 25;
			if (health <= 0) {
				endGame();
			}
		}
	}

	private void endGame () {
		Time.timeScale = 0.4f;
		// HOTween.To(Time, 0.1, "timeScale", 0.2f);
		// gameEngine.GetComponent<GameEngine>().mainCamera.transform.position = new Vector3(transform.position.x, transform.position.y, -10);
		HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 3, "position", new Vector3(transform.position.x, transform.position.y, -10));
		HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.transform, 3, "localRotation", Quaternion.Euler(0, 0, 30));
		// gameEngine.GetComponent<GameEngine>().mainCamera.camera.orthographicSize = 2;
		HOTween.To(gameEngine.GetComponent<GameEngine>().mainCamera.camera, 3, "orthographicSize", 2);

		PlayerPrefs.SetInt ("totalZombiesKilled", GameEngine.totalZombiesKilled);

		// showInterface();

		// int numOfParts = (int) (Random.value * 50 + 50);
		int numOfParts = GameEngine.maxParticleCount - GameEngine.particleCount;
		for(int k = 0; k < numOfParts;k++){
			if(GameEngine.particleCount > GameEngine.maxParticleCount)
				break;
			GameObject _part = (GameObject) Instantiate(prefabParticle);
			_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
			_part.GetComponent<moveParticle>().vel = Random.value * 5 + 6;
			GameEngine.particleCount++;
		}

		Destroy(gameObject);

		GameEngine.gameOver = true;
		awardAchievements();
	}

	void awardAchievements() {
		if (GameEngine.totalZombiesKilled > 1) {
			// give "Zombie Slayer"
			Social.ReportProgress("CgkIgpP17bANEAIQAg", 100.0f, (bool success) => {
				// handle success or failure
			});
		}
		if (GameEngine.totalZombiesKilled > 50) {
			// give "BETTER ZOMBIE SLAYER"
			Social.ReportProgress("CgkIgpP17bANEAIQAw", 100.0f, (bool success) => {
				// handle success or failure
			});
		}
		if (GameEngine.totalZombiesKilled > 500) {
			// give "Super Zombie Slayer"
			Social.ReportProgress("CgkIgpP17bANEAIQBA", 100.0f, (bool success) => {
				// handle success or failure
			});
		}
		if (GameEngine.totalZombiesKilled > 5000) {
			// give "Ultimate Zombie Slayer"
			Social.ReportProgress("CgkIgpP17bANEAIQBQ", 100.0f, (bool success) => {
				// handle success or failure
			});
		}
		if (GameEngine.totalZombiesKilled > 500000) {
			// give "Supreme Overlord Zombie Slayer"
			Social.ReportProgress("CgkIgpP17bANEAIQBg", 100.0f, (bool success) => {
				// handle success or failure
			});
		}
		if (GameEngine.totalScore > 5000) {
			// give "High Scorer"
			Social.ReportProgress("CgkIgpP17bANEAIQBw", 100.0f, (bool success) => {
				// handle success or failure
			});
		}
		if (GameEngine.totalScore > 20000) {
			// give "Higher Scorer"
			Social.ReportProgress("CgkIgpP17bANEAIQCA", 100.0f, (bool success) => {
				// handle success or failure
			});
		}
		Social.ReportScore(GameEngine.totalScore, "CgkIgpP17bANEAIQCQ", (bool success) => {
        	// handle success or failure
    	});
	}

	void shootBullet() {
		bool laserShooting = false;
		bool twoshot = false;
		bool shotgun = false;
		
		for(int i = 0;i < collectedPowerups.Count;i++){
			if(collectedPowerups[i] == Powerup.PowerupType.LASER){
				laserShooting = true;
			}
			else if(collectedPowerups[i] == Powerup.PowerupType.TWOSHOT){
				if(twoshotAlive < twoshotLife){
					twoshotAlive += 1;
					twoshot = true;
				}
				else{
					collectedPowerups.Remove(Powerup.PowerupType.TWOSHOT);
					twoshotAlive = 0;
				}
			}
			else if(collectedPowerups[i] == Powerup.PowerupType.SHOTGUN){
				if(shotgunAlive < shotgunLife){
					shotgunAlive += 1;
					shotgun = true;
				}
				else{
					collectedPowerups.Remove(Powerup.PowerupType.SHOTGUN);
					shotgunAlive = 0;
				}
			}
		}
		if(laserShooting){
//			if(laser.alive <= laser.life){
//				laser.move(transform.position.x + diameter / 2 ,transform.position.y + diameter / 2 ,_mouseX,_mouseY,screenWidth,screenHeight);
//				laser.alive++;
//			}
//			else{
//				for(int i = 0;i < collectedPowerups.Count;i++){
//					if(collectedPowerups[i] == Powerup.PowerupType.LASER){
//						collectedPowerups.remove(i);
//						laser.alive = 0;
//					}
//				}
//			}
		}
		else{
			//remove 1 point for using ammo
			GameEngine.totalScore--;
			
			GameObject _bullet = (GameObject) Instantiate (prefabBullet);
			_bullet.transform.position = new Vector3(transform.position.x, transform.position.y, 0);
			_bullet.transform.LookAt (new Vector3((Input.acceleration.x - gameGUI.calibratedRotation.x) * 1000, (Input.acceleration.y - gameGUI.calibratedRotation.y) * 1000, 0));

			if(twoshot){
				GameObject _bullet2 = (GameObject) Instantiate (prefabBullet);
				_bullet2.transform.position = new Vector3(transform.position.x,transform.position.y,0);
				_bullet2.transform.LookAt (new Vector3((Input.acceleration.x - gameGUI.calibratedRotation.x) * -1000, (Input.acceleration.y - gameGUI.calibratedRotation.y) * -1000, 0));
			}
			if(shotgun){
				for(int i = -2;i <= 2;i++){
					if (i != 0) {
						GameObject _bullet2 = (GameObject) Instantiate (prefabBullet);
						_bullet2.transform.position = new Vector3(transform.position.x,transform.position.y,0);
						float _angleTo = Mathf.Atan2((Input.acceleration.y - gameGUI.calibratedRotation.y), (Input.acceleration.x - gameGUI.calibratedRotation.x)) + ((float) i / 5);
						_bullet2.transform.LookAt (new Vector3(Mathf.Cos(_angleTo) * 1000, Mathf.Sin(_angleTo) * 1000, 0));
					}
				}
			}
		}
	}
}