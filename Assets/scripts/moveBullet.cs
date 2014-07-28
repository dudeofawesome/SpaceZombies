using UnityEngine;
using System.Collections;

public class moveBullet : MonoBehaviour {
	public int velocity = 20;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void FixedUpdate () {
		transform.position = new Vector3 ((float)(transform.position.x + transform.forward.x * 0.01f * velocity), (float)(transform.position.y + transform.forward.y * 0.01f * velocity), 0);

		// if (!GameEngine.cameraViewSize.Contains(new Vector2(transform.position.x, transform.position.y))) {
		if (transform.position.x < GameEngine.cameraViewSize.x || transform.position.y > GameEngine.cameraViewSize.y || transform.position.x > GameEngine.cameraViewSize.width || transform.position.y < GameEngine.cameraViewSize.height) {
			Destroy(gameObject);
		}
	}
}
