import Entitys.{Mob, Player, Position}
import Items.{Consumable, Placable}

/**
 * sealed trait Request csak ebben a fájlban extendelhető
 */
sealed trait Request

/**
 * Tick egy speciális request
 */

case object Tick extends Request

/**
 * Join egy speciális request
 * @param player, aki be akar lépni a játékból.
 */

case class Join(player: Player) extends Request

/**
 * LeavePlayer egy speciális request
 * @param id aki ki akar lépni a játékból.
 */

case class LeavePlayer(id: String) extends  Request

/**
 * Die egy speciális request
 * @param id, a játékos id-je akit meg ölük..
 */

case class Die(id: String) extends  Request

/**
 * Mine egy speciális request
 * @param id, a játékos id-je aki bányászik.
 * @param position, a játékos poziciója
 */

case class Mine(id: String, position: Position) extends  Request

/**
 * StoreItem egy speciális request
 * @param playerID, a játékos id-je.
 * @param chestID, a játékoshoz tartozó chest id-je.
 */

case class StoreItem(playerID: String, chestID: String) extends  Request

/**
 * LootItem egy speciális request
 * @param playerID, a játékos id-je .
 * @param chestID, a játékoshoz tartozó chest id-je.
 * @param index, indexet jelöli
 */

case class LootItem(playerID: String, chestID: String, index: Int) extends  Request

/**
 * Consume egy speciális request
 * @param playerID, a játékos id-je .
 */

case class Consume(playerID: String) extends  Request

/**
 * MoveEntity egy speciális request
 * @param playerID, a játékos id-je .
 * @param position, a játékos poziciója
 */

case class MoveEntity(playerID: String, position: Position) extends  Request

/**
 * HitEntity egy speciális request
 * @param attackerID, a támadó id-je
 * @param defenderID, a védekező id-je
 */

case class HitEntity(attackerID: String, defenderID: String) extends  Request

/**  * A WordState objektum fogja tárolni a világot.
 *
 *@param request a világban jelenlévő requestek
 * @param player  a világban jelenlévő játékosok
 * @param mob a világban jelenlévő mobok.
 */

case class WorldState(req: Vector[Request], player: Vector[Player], mob: Vector[Mob], map:Array[Array[Placable]]){
  /**
   * Megnezi van e meg feldolgozott request, es ha van visszaadja
   *
   * @return true ha van, false ha nincs
   */
  def hasRequests(): Boolean = req.nonEmpty

  /**
   * Soron következő requestet dolgozza fel
   *
   * @return ha nincs több feldolgozatlan request, vagy nincs benn player a játékban, akkor adjuk vissza az eredeti statet
   */

  def proccesNextNextRequest():  WorldState = {
    if (!hasRequests() || player.isEmpty) this
    else req.head match {
      case Join(p) => {
        val newPv = player :+ p
        WorldState(req, newPv, mob, map)
      }
      case LeavePlayer(id) => WorldState(req, player.filter(_.id == id), mob, map)
      case Die(id) => WorldState(req, player, mob.filter(_.id == id), map)
      case StoreItem(playerID, chestID) => {
        val playerO = player.find(_.id == playerID)
        val chestO = player.find(_.inventory.id == chestID)
        if (playerO.isDefined && chestO.isDefined) {
          val player = playerO.get
          val chest = chestO.get
          val emptyC = playerO.get.onCursor.isEmpty
          if (!emptyC) {
            val item = player.peekCursor().get
            val res = chest.inventory.+(item)
            player.onCursor.take(res._2.get.db)
          }
        }
        this
      }
      case LootItem(playerID, chestID, index) => {
        val playerOpt = player.find(_.id == playerID)
        val chestOpt = player.find(_.inventory.id == chestID)
        if (playerOpt.isDefined && chestOpt.isDefined) {
          val emptyCursor = playerOpt.get.onCursor.isEmpty
          if (emptyCursor) {
            val itemOpt = chestOpt.get.inventory.apply(index)
            val playerOpt.get.onCursor = Some(itemOpt.get)
          }
        }
        this
      }
      case Tick => {
        player.map(_.tick())
        mob.map(_.tick())
        val (newRequests, finalPlayers, finalMobs) = removeDEntities(req.tail, player, mob)
        val deadPlayers = player.filter(!_.isAlive())
        deadPlayers.foreach { p => req :+= Die(p.id) }
        val deadMobs = mob.filter(!_.isAlive())
        deadMobs.foreach { m => req :+= Die(m.id) }
        WorldState(newRequests, finalPlayers, finalMobs, map)
      }
      case Consume(playerID) => {
        val playerOpt = Option(player(player.indexWhere(_.id == playerID)))
        playerOpt.flatMap(_.onCursor.collect { case c: Consumable => c }) match {
          case Some(consumable) =>
            val updateP = playerOpt.get.copy(onCursor = None, activeEffect = playerOpt.get.activeEffect ++ consumable.effects)
            WorldState(req, player.updated(player.indexOf(playerOpt.get), updateP), mob, map)
          case None => this
        }
      }
      case HitEntity(attackerID, defenderID) => {
        val attackerOpt = Option(player.filter(_.id == attackerID))
        val defenderOpt = Option(mob.filter(_.id == defenderID))

        (attackerOpt, defenderOpt) match {
          case (Some(attacker: Player), Some(defender: Mob)) =>
            val damage = Math.max(1, attacker.baseStat.attack - defender.baseStat.defense)
            val updateD = defender.copy(currentHP = Math.max(0, defender.currentHP - damage))

            if (updateD.currentHP == 0) {
              val dieR = Die(updateD.id)
              WorldState(req :+ dieR, players(), mob.filterNot(_.id == defenderID), map)
            } else {
              WorldState(req, players(), mob.updated(mob.indexOf(updateD), updateD.asInstanceOf[Mob]), map)
            }
          case _ => this
        }
      }
    }
  }

  def removeDEntities(requests: Vector[Request], players: Vector[Player], mobs: Vector[Mob]): (Vector[Request], Vector[Player], Vector[Mob]) = {
    val deadPids = players.filter(!_.isAlive).map(_.id).toSet
    val deadMids = mobs.filter(!_.isAlive).map(_.id).toSet

    val finalP = players.filter(_.isAlive())
    val finalM= mobs.filter(_.isAlive())

    val deadPReq = requests ++ deadPids.map(Die)
    val deadMReq = deadPReq ++ deadMids.map(Die)

    (deadMReq, finalP, finalM)
  }


  /**
   * Az összes requestet dolgozza fel
   *
   * @return Visszaadja a state, ha elfogxnak a requestek
   */

  def proccesAllNextRequest():  WorldState = {
    var nextS = this
    while (nextS.hasRequests()) {
      nextS = nextS.proccesNextNextRequest()
    }
    nextS
  }

  /**
   * A világban aktuálisan belépett játékosok
   *
   * @return a világban aktuálisan belépett játékosok
   */

  def players() : Vector[Player] = player

  /**
   * Visszaadja az adott térképen koordinátáján lévő blokkot egy Optional[Placeble]
   * @param x a térkép x koordinátája
   * @param y a térkép x koordinátája
   * @return ha nincs ezen pozicíón blokk akkor NONE egyébként az ott lévő Placeble egy Someba
   */

  def apply(x: Int, y: Int): Option[Placable] = {
    if (x >= 0 && x < width() && y >= 0 && y < height()) {
      Some(map(y)(x))
    } else {
      None
    }
  }

  /**
   * Visszaadja az adott pozicíón lévő blokkot egy Optional[Placeble]
   *
   * @param position a térkép poizicíója
   * @return ha nincs ezen pozicíón blokk akkor NONE egyébként az ott lévő Placeble egy Someba
   */

  def apply(position: Position): Option[Placable] = {
    if (position.x < 0 || position.y < 0 || position.x >= width || position.y >= height) None
    else map.lift(position.y.toInt).flatMap(_.lift(position.x.toInt))
  }

  def height(): Int = map.length

  def width(): Int = map(0).length

}