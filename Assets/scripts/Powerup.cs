using UnityEngine;
using System.Collections;

public class Powerup : MonoBehaviour {
	public enum PowerupType {
		TWOSHOT,
		HEALTH,
		LASER,
		NUKE,
		SHIELD,
		SHOTGUN
	}
	public PowerupType type = PowerupType.TWOSHOT;

	int smokeCounter = 0;

	[SerializeField] private GameObject prefabParticle = null;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void FixedUpdate () {
		if (!GameEngine.gameOver) {
			if(smokeCounter >= 4){
				GameObject _part = (GameObject) Instantiate(prefabParticle);
				_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
				_part.GetComponent<moveParticle>().changeType(moveParticle.pType.POWERUPDUST);
				GameEngine.particleCount++;
				smokeCounter = 0;
			}
			else{
				smokeCounter ++;
			}
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
    		GameEngine.totalScore += 400;
			if (type == PowerupType.HEALTH)
				movePlayer.health += 25;
			else if (type == PowerupType.NUKE) {
				GameObject[] objects = GameObject.FindGameObjectsWithTag("Enemy");
				for (int i = 0; i < objects.Length; i++){
    				Destroy(objects[i]);
    				GameEngine.totalZombiesKilled++;
    				GameEngine.totalScore += 100;
				}
				int numOfParts = (int) (Random.value * 10 + 30);
				for(int k = 0; k < numOfParts;k++){
					if(GameEngine.particleCount < GameEngine.maxParticleCount) {
						GameObject _part = (GameObject) Instantiate(prefabParticle);
						_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
						_part.GetComponent<moveParticle>().vel = Random.value * 5 + 13;
						GameEngine.particleCount++;
					}
				}
			}
			else {
				GameObject.FindGameObjectWithTag("Player").GetComponent<movePlayer>().collectedPowerups.Add(type);
			}
			int nop = (int) (Random.value * 5 + 10);
			for(int k = 0; k < nop;k++){
				if(GameEngine.particleCount < GameEngine.maxParticleCount) {
					GameObject _part = (GameObject) Instantiate(prefabParticle);
					_part.transform.position = new Vector3(transform.position.x, transform.position.y, 1);
					_part.GetComponent<moveParticle>().vel = Random.value * 5 + 3;
					GameEngine.particleCount++;
				}
			}
			Destroy(gameObject);
		}
	}
}