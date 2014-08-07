using UnityEngine;
using System.Collections;

public class moveEnemy : MonoBehaviour {
	public int health = 100;
	public int velocity = 5;
	public GameObject player;

	[SerializeField] private GameObject prefabParticle = null;
	[SerializeField] private GameObject prefabPowerup2shot = null;
	[SerializeField] private GameObject prefabPowerupHealth = null;
	[SerializeField] private GameObject prefabPowerupLaser = null;
	[SerializeField] private GameObject prefabPowerupNuke = null;
	[SerializeField] private GameObject prefabPowerupShield = null;
	[SerializeField] private GameObject prefabPowerupShotgun = null;

	// Use this for initialization
	void Start () {
		// player = GameObject.Find ("Player");
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void FixedUpdate () {
		if (!GameEngine.gameOver) {
			transform.LookAt (player.transform.position);
			transform.position = new Vector3 ((float)(transform.position.x + transform.forward.x * 0.01f * velocity), (float)(transform.position.y + transform.forward.y * 0.01f * velocity), 0);
		}
	}

	void OnTriggerEnter2D (Collider2D collision) {
		MyOnCollisionEnter(collision.gameObject);
	}
	
	void OnCollisionEnter2D (Collision2D collision) {
		MyOnCollisionEnter(collision.gameObject);
	}

	void MyOnCollisionEnter (GameObject go) {
		if (go.tag == "Player") {
			addParticlesOnDeath();
			// if (GameObject.FindGameObjectWithTag("Player").GetComponent<movePlayer>().health > 0)
			// 	reset();
			if (movePlayer.twoshotAlive > 0 || movePlayer.shotgunAlive > 0 || movePlayer.shieldAlive > 0)
				Destroy(gameObject);
			else
				reset();
		}
		else if (go.tag == "Bullet") {
			addParticlesOnDeath();
			addPowerupsOnDeath();
			GameEngine.totalZombiesKilled++;
    		GameEngine.totalScore += 100;
			if (movePlayer.twoshotAlive > 0 || movePlayer.shotgunAlive > 0 || movePlayer.shieldAlive > 0)
				Destroy(gameObject);
			else
				reset();
		}
	}

	void addPowerupsOnDeath () {
		int randomNumber = (int) (Random.value * 250);
		// int randomNumber = (int) (Random.value * 2);
		if(randomNumber == 1){
			//add new powerup
			GameObject _pu;
			switch((int) (Random.value * 12)){
				case 0: case 1:
					_pu = (GameObject) Instantiate(prefabPowerupLaser);
					_pu.transform.position = new Vector3(transform.position.x, transform.position.y, -0.5f);
					_pu.GetComponent<Powerup>().type = Powerup.PowerupType.LASER;
					break;
				case 2:
					_pu = (GameObject) Instantiate(prefabPowerupNuke);
					_pu.transform.position = new Vector3(transform.position.x, transform.position.y, -0.5f);
					_pu.GetComponent<Powerup>().type = Powerup.PowerupType.NUKE;
					break;
				case 3: case 4: case 5: case 6:
					_pu = (GameObject) Instantiate(prefabPowerup2shot);
					_pu.transform.position = new Vector3(transform.position.x, transform.position.y, -0.5f);
					_pu.GetComponent<Powerup>().type = Powerup.PowerupType.TWOSHOT;
					break;
				case 7: case 8: case 9:
					_pu = (GameObject) Instantiate(prefabPowerupShotgun);
					_pu.transform.position = new Vector3(transform.position.x, transform.position.y, -0.5f);
					_pu.GetComponent<Powerup>().type = Powerup.PowerupType.SHOTGUN;
					break;
				case 10:
					_pu = (GameObject) Instantiate(prefabPowerupHealth);
					_pu.transform.position = new Vector3(transform.position.x, transform.position.y, -0.5f);
					_pu.GetComponent<Powerup>().type = Powerup.PowerupType.HEALTH;
					break;
				case 11:
					_pu = (GameObject) Instantiate(prefabPowerupShield);
					_pu.transform.position = new Vector3(transform.position.x, transform.position.y, -0.5f);
					_pu.GetComponent<Powerup>().type = Powerup.PowerupType.SHIELD;
					break;
			}
		}
	}

	void addParticlesOnDeath () {
		int numOfParts = (int) (Random.value * 5 + 7);
		for(int k = 0; k < numOfParts;k++){
			if(GameEngine.particleCount < GameEngine.maxParticleCount) {
				GameObject _part = (GameObject) Instantiate(prefabParticle);
				_part.transform.position = new Vector3(transform.position.x, transform.position.y, Random.value + 0.5f);
				GameEngine.particleCount++;
			}
		}
	}

	public void reset(){
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
		Vector3 _pos = GameObject.Find("Main Camera").camera.ScreenToWorldPoint(new Vector3(_xLoc, _yLoc, Mathf.Abs(GameObject.Find("Main Camera").transform.position.z)));
		transform.position = new Vector3 (_pos.x, _pos.y, 0);
		transform.localScale = new Vector3 (_diameter, _diameter, _diameter);
		velocity = _totalV;
	}
}