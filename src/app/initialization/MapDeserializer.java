package app.initialization;

import app.interactables.Enemy;
import app.interactables.EnemyType;
import app.interactables.GunType;
import app.interactables.Interactable;
import app.interactables.Key;
import app.interactables.SwordType;
import app.interactables.Weapon;
import app.resourcehandlers.Texture;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import app.mechanics.InteractableFactory;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * MapDeserializer is used to deserialize maps from a Json-file containing map-data
 *
 * @author Oliver Björklund
 * @version 1.0
 */


public class MapDeserializer implements JsonDeserializer<GameMap> {
    private static final Logger LOGGER = Logger.getLogger("GameLogger");
    @Override public GameMap deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject obj = json.getAsJsonObject();

//	Standard deserialization
	int size = context.deserialize(obj.get("Size"), int.class);
	int[][] mapDesign = context.deserialize(obj.get("Layout"), int[][].class);

//	Custom deserialization
	Point startPos = createPoint(obj.get("StartPos"), context);
	Dimension blockSize = createDimension(obj.get("BlockSize"), context);

	Point keyPos = createPoint(obj.get("KeyPos"), context);
	Key key = new Key(keyPos, new Dimension(20, 20), true, new Texture("images/key.png"));

	List<Weapon> weapons = createInteractables(obj.get("Weapons"), context, Weapon.class);
	List<Enemy> enemies = createInteractables(obj.get("Enemies"), context, Enemy.class);
	List<Interactable> objects = new ArrayList<>();
	objects.addAll(enemies);
	objects.addAll(weapons);
	objects.add(key);

	LOGGER.config("Map successfully deserialized");
	return new GameMap(size, mapDesign, blockSize, objects, enemies, weapons, key, startPos);
    }

    private Dimension createDimension(JsonElement el, JsonDeserializationContext context) {
	int[] arr = context.deserialize(el, int[].class);
	return new Dimension(arr[0], arr[1]);
    }

    private Point createPoint(JsonElement el, JsonDeserializationContext context) {
	int[] arr = context.deserialize(el, int[].class);
	return new Point(arr[0], arr[1]);
    }

    private <T extends Interactable> List<T> createInteractables(JsonElement el, JsonDeserializationContext context, Class<T> intClass) {
	InteractableFactory factory = new InteractableFactory();

	List<T> res = new ArrayList<>();
	JsonArray jsonArr = el.getAsJsonArray();

	for (JsonElement jsonElement : jsonArr) {
	    JsonObject jsonObj = jsonElement.getAsJsonObject();
	    String type = jsonObj.get("Type").getAsString();
	    Point pos = createPoint(jsonObj.get("Pos"), context);
	    Dimension size = createDimension(jsonObj.get("Size"), context);

	    T item = intClass.cast(switch(type) {
		case "GUN" -> factory.createGun(GunType.GUN, pos, size, true);
		case "RIFLE" -> factory.createGun(GunType.RIFLE, pos, size, true);
		case "CROSSBOW" -> factory.createGun(GunType.CROSSBOW, pos, size, true);
		case "BOMB" -> factory.createGun(GunType.BOMB, pos, size, true);
		case "MINECRAFT_SWORD" -> factory.createSword(SwordType.MINECRAFT_SWORD, pos, size, true);
		case "SCYTHE" -> factory.createSword(SwordType.SCYTHE, pos, size, true);
		case "DAGGER" -> factory.createSword(SwordType.DAGGER, pos, size, true);
		case "FROSTMOURNE" -> factory.createSword(SwordType.FROSTMOURNE, pos, size, true);
		case "RED_AMONGUS" -> factory.createEnemy(EnemyType.RED_AMONG_US, pos, size, true);
		case "GHOST" -> factory.createEnemy(EnemyType.GHOST, pos, size, true);
		case "ALIEN" -> factory.createEnemy(EnemyType.ALIEN, pos, size, true);
		case "JAVA" -> factory.createEnemy(EnemyType.JAVA, pos, size, true);

		default -> throw new IllegalArgumentException("a");
	    });
	    res.add(item);
	}
	return res;
    }
}
