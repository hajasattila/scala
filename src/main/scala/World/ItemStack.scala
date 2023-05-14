package World
import Items.Item

/**
 * ItemStack csak úgy tudjuk létrehozni, ha a darbszám nemnegatív, és legfeljebb annyi, mint az itemnek a maxStackSizeon
 * @param item Itemet tárol
 * @param db darabszámot tárol
 */
case class ItemStack(item: Item, db: Int) {
  require(db >= 0 && db <= item.maxStackSize, "Hibás az itemStack értéke")

  /**
   * Megpróbálja egy stackbe rakni a két itemstacket
   *
   * @param that a másik stack amit egyesíteni akarok a jelenlegivel
   * @return Ha a két stack itemjei különböznek, akkor adjuk vissza az első koordinátán
   * a bal, a másodikon a jobb oldali eredeti stacket Optionba
   * ha egyformák, és az összes mennyiségük elfér egyben, akkor a visszaadott tuple első koordinátájába kerüljön
   * az egyberakott stack, a másodikba pedig kerüljön None, ha nem fér el, akkor a bal oldaliba
   * rakjunk amennyit lehet, a jobb oldaliba pedig a maradékot!
   */
  def +(that: ItemStack): (ItemStack, Option[ItemStack]) = {
    if (this.item != that.item) {
      (this, Some(that))
    } else {
      val sum = this.db + that.db
      if (sum <= this.item.maxStackSize) {
        (ItemStack(this.item, sum), None)
      } else {
        val amountToAdd = this.item.maxStackSize - this.db
        (ItemStack(this.item, this.item.maxStackSize), Some(ItemStack(this.item, sum - amountToAdd)))
      }
    }
  }

  def isEmpty: Boolean = db == 0

  def peek(amount: Int): Option[ItemStack] = {
    if (isEmpty || amount <= 0) {
      None
    } else if (this.db <= amount) {
      Some(copy())
    } else {
      Some(ItemStack(item, amount))
    }
  }

  def take(amount: Int): Option[ItemStack] = {
    if (amount <= 0 || isEmpty) {
      None
    } else if (this.db <= amount) {
      val result = Some(copy())
      copy(db = this.db - amount)
      result
    } else {
      val result = Some(ItemStack(item, amount))
      this.db -= amount
      result
    }
  }

}