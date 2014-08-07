using UnityEngine;
using System.Collections;

public class moveParticle : MonoBehaviour {
	public enum pType {EXPLOSION, SMOKE, FIRE, EXHAUST, POWERUPDUST};

	public float dir = 0;
	public float vel = 1;
	public int life = 100;
	public int alive = 0;
	public int diameter = 5;
	public pType type = pType.EXPLOSION;
	public float randomness = 0.1f;
	public float opacityStep = 1;
	public float opacity = 255;
	public Color color = Color.red;
	public Color[] colors = new Color[10];
	public int positionInColors = 9;
	public int maxPositionInColors = 9;

	[SerializeField] private GameObject mesh = null;

	// Use this for initialization
	void Start () {
		if (type == pType.EXPLOSION) {
			colors = new Color[10];
			colors[0] = new Color(209,0,0);
			colors[1] = new Color(219,29,0);
			colors[2] = new Color(222,52,4);
			colors[3] = new Color(232,99,5);
			colors[4] = new Color(240,138,5);
			colors[5] = new Color(252,179,20);
			colors[6] = new Color(255,213,25);
			colors[7] = new Color(255,255,46);
			colors[8] = new Color(255,255,102);
			colors[9] = new Color(255,255,176);
			color = colors[9];
			positionInColors = 9;
			maxPositionInColors = 9;
			dir = Random.value * 2.0f * Mathf.PI;
			diameter = (int) (Random.value * 5 + 5);
			opacity = (int) (Random.value * 100 + 140);
			opacityStep = opacity / life;
		}
		if(vel == 1)
			vel = Random.value * 3 + 0.3f;

		transform.localScale = new Vector3(diameter / 5f, diameter / 5f, diameter / 5f);
		// transform.localScale = new Vector3(0.1f,0.1f,0.1f);

		transform.position = new Vector3(transform.position.x, transform.position.y, Random.value + 0.5f);
	}

	public void changeType (pType type) {
		colors = new Color[10];
		this.type = type;

		if(type == pType.EXPLOSION){
			color = colors[9];
			positionInColors = 9;
			maxPositionInColors = 9;

			dir = Random.value * 2.0f * Mathf.PI;
			vel = Random.value * 3 + 0.3f;
			diameter = (int) (Random.value * 5 + 5);
			opacity = (int) (Random.value * 100 + 140);
			opacityStep = opacity / life;
		}
		else if(type == pType.SMOKE){
			colors[0] = new Color(12,12,12);
			colors[1] = new Color(20,20,20);
			colors[2] = new Color(25,25,25);
			colors[3] = new Color(30,30,30);
			colors[4] = new Color(41,41,41);
			colors[5] = new Color(60,60,60);
			colors[6] = new Color(80,80,80);
			colors[7] = new Color(100,100,100);
			colors[8] = new Color(120,120,120);
			colors[9] = new Color(148,148,148);
			int _position = (int) (Random.value * 9);
			color = colors[_position];
			positionInColors = _position;
			maxPositionInColors = _position;

			dir = Random.value * 2.0f * Mathf.PI;
			vel = Random.value * 4.0f + 1;
			diameter = (int) (Random.value * 10 + 5);
			opacity = (int) (Random.value * 20 + 130);
			opacityStep = opacity / life;
		}
		else if(type == pType.FIRE){
			colors[0] = new Color(214,39,0);
			colors[1] = new Color(242,44,0);
			colors[2] = new Color(242,69,0);
			colors[3] = new Color(255,106,0);
			colors[4] = new Color(255,157,0);
			colors[5] = new Color(255,174,0);
			colors[6] = new Color(255,179,0);
			colors[7] = new Color(255,236,28);
			colors[8] = new Color(255,245,135);
			colors[9] = new Color(255,251,204);
			color = colors[9];
			positionInColors = 9;
			maxPositionInColors = 9;

			dir = Random.value * 2.0f * Mathf.PI;
			vel = Random.value * 4.0f + 0.1f;
			diameter = (int) (Random.value * 4 + 3);
			opacity = (int) (Random.value * 50 + 150);
			opacityStep = opacity / life;
		}
		else if(type == pType.EXHAUST){
			colors[0] = new Color(55,0,100);
			colors[1] = new Color(40,0,75);
			colors[2] = new Color(33,0,60);
			colors[3] = new Color(23,0,41);
			colors[4] = new Color(16,0,29);
			int _position = (int) (Random.value * 5);
			color = colors[_position];
			positionInColors = _position;
			maxPositionInColors = _position;

			dir = Random.value * 2.0f * Mathf.PI;
			vel = Random.value * 4.0f + 0.1f;
			diameter = (int) (Random.value * 11 + 2);
			life = 70;
			opacity = (int) (Random.value * 50 + 170);
			opacityStep = opacity / life;
		}
		else if(type == pType.POWERUPDUST){
			colors[0] = new Color(55,0,100);
			colors[1] = new Color(40,0,75);
			colors[2] = new Color(33,0,60);
			colors[3] = new Color(23,0,41);
			colors[4] = new Color(16,0,29);
			int _position = (int) (Random.value * 5);
			color = colors[_position];
			positionInColors = _position;
			maxPositionInColors = _position;

			dir = Random.value * 2.0f * Mathf.PI;
			vel = Random.value * 4.0f + 0.1f;
			diameter = (int) (Random.value * 5 + 5);
			life = 70;
			opacity = (int) (Random.value * 50 + 200);
			opacityStep = opacity / life;
		}
	}
	
	void FixedUpdate () {
		if(alive > life){
			Destroy(gameObject);
		}
		else{
			float _dX = (Mathf.Cos(dir) * vel / 100);
			float _dY = (Mathf.Sin(dir) * vel / 100);
			transform.position = new Vector3(transform.position.x + _dX, transform.position.y + _dY, transform.position.z);
			dir += Random.value * 0.6f - 0.3f;
			vel *= 0.99f;
			if(opacity - 1 > 0){
				opacity -= opacityStep;
			}
			if(type == pType.EXPLOSION || type == pType.FIRE){
				switch(alive){
					case 10:
						color = colors[8];
						positionInColors = 8;
					break;
					case 20:
						color = colors[7];
						positionInColors = 7;
					break;
					case 30:
						color = colors[6];
						positionInColors = 6;
					break;
					case 40:
						color = colors[5];
						positionInColors = 5;
					break;
					case 50:
						color = colors[4];
						positionInColors = 4;
					break;
					case 60:
						color = colors[3];
						positionInColors = 3;
					break;
					case 70:
						color = colors[2];
						positionInColors = 2;
					break;
					case 80:
						color = colors[1];
						positionInColors = 1;
					break;
					case 90:
						color = colors[0];
						positionInColors = 0;
					break;
				}
			}
			else{
				for (int i = 1; i < maxPositionInColors; i++) {
					if (alive == (int) (life * i / maxPositionInColors)) {
						positionInColors--;
						color = colors[positionInColors];
					}
				}
			}

			alive ++;
		}

		// gameObject.renderer.material.color = new ColorHSV(color,1f,1f,opacity / 255f).ToColor();
		Color _color = new Color(color.r/255,color.g/255,color.b/255,opacity/255);
		mesh.renderer.material.color = _color;
	}

	void OnDestroy () {
		GameEngine.particleCount--;
	}
}
