package Items

/**
 * Egy olyan case class, amibe a dedikált weaponok találhatóak, ami az Item-ből extends-el
 * maximum egy Weapon-je lehet
 * @param name Item name
 * @param maxStackSize inventory slot maxmimuma egy adott Itemnek
 * @param damage sebzésre szolgál
 */
case class Weapon(name: String, maxStackSize: Int = 1, damage: Int) extends Item