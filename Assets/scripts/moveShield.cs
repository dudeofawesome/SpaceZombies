using UnityEngine;
using System.Collections;

public class moveShield : MonoBehaviour {
	public int life = 400;

	[SerializeField] private GameObject player = null;

	// Use this for initialization
	void Start () {
		player = GameObject.FindGameObjectWithTag("Player");
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void FixedUpdate () {
		life--;
		if (life < 0) {
			Destroy(gameObject);
		}

		transform.position = player.transform.position;
	}
}
