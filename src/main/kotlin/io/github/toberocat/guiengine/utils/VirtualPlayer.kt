package io.github.toberocat.guiengine.utils

import org.bukkit.*
import org.bukkit.advancement.Advancement
import org.bukkit.advancement.AdvancementProgress
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.PistonMoveReaction
import org.bukkit.block.Sign
import org.bukkit.block.data.BlockData
import org.bukkit.conversations.Conversation
import org.bukkit.conversations.ConversationAbandonedEvent
import org.bukkit.entity.*
import org.bukkit.entity.memory.MemoryKey
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import org.bukkit.inventory.*
import org.bukkit.map.MapView
import org.bukkit.metadata.MetadataValue
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.profile.PlayerProfile
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.util.BoundingBox
import org.bukkit.util.RayTraceResult
import org.bukkit.util.Vector
import java.net.InetSocketAddress
import java.util.*

/**
 * Created: 06.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
class VirtualPlayer : Player {
    override fun getDisplayName(): String = ""

    override fun setDisplayName(name: String?) {}
    override fun getPlayerListName(): String = ""

    override fun setPlayerListName(name: String?) {}
    override fun getPlayerListHeader(): String? {
        return null
    }

    override fun getPlayerListFooter(): String? {
        return null
    }

    override fun setPlayerListHeader(header: String?) {}
    override fun setPlayerListFooter(footer: String?) {}
    override fun setPlayerListHeaderFooter(header: String?, footer: String?) {}
    override fun setCompassTarget(loc: Location) {}
    override fun getCompassTarget(): Location = Location(null, 0.0, 0.0, 0.0)

    override fun getAddress(): InetSocketAddress? {
        return null
    }

    override fun isConversing(): Boolean {
        return false
    }

    override fun acceptConversationInput(input: String) {}
    override fun beginConversation(conversation: Conversation): Boolean {
        return false
    }

    override fun abandonConversation(conversation: Conversation) {}
    override fun abandonConversation(conversation: Conversation, details: ConversationAbandonedEvent) {}
    override fun sendRawMessage(message: String) {}
    override fun sendRawMessage(uuid: UUID?, s: String) {}
    override fun kickPlayer(message: String?) {}
    override fun chat(msg: String) {}
    override fun performCommand(command: String): Boolean {
        return false
    }

    override fun getLocation(): Location = Location(null, 0.0, 0.0, 0.0)
    override fun getLocation(loc: Location?): Location? {
        return null
    }

    override fun setVelocity(velocity: Vector) {}
    override fun getVelocity(): Vector = Vector()

    override fun getHeight(): Double {
        return 0.0
    }

    override fun getWidth(): Double {
        return 0.0
    }

    override fun getBoundingBox(): BoundingBox = BoundingBox()

    override fun isOnGround(): Boolean {
        return false
    }

    override fun isInWater(): Boolean {
        return false
    }

    override fun getWorld(): World = null!!

    override fun setRotation(yaw: Float, pitch: Float) {}
    override fun teleport(location: Location): Boolean {
        return false
    }

    override fun teleport(location: Location, cause: TeleportCause): Boolean {
        return false
    }

    override fun teleport(destination: Entity): Boolean {
        return false
    }

    override fun teleport(destination: Entity, cause: TeleportCause): Boolean {
        return false
    }

    override fun getNearbyEntities(x: Double, y: Double, z: Double): List<Entity> {
        return emptyList()
    }

    override fun getEntityId(): Int {
        return 0
    }

    override fun getFireTicks(): Int {
        return 0
    }

    override fun getMaxFireTicks(): Int {
        return 0
    }

    override fun setFireTicks(ticks: Int) {}
    override fun setVisualFire(b: Boolean) {}
    override fun isVisualFire(): Boolean {
        return false
    }

    override fun getFreezeTicks(): Int {
        return 0
    }

    override fun getMaxFreezeTicks(): Int {
        return 0
    }

    override fun setFreezeTicks(i: Int) {}
    override fun isFrozen(): Boolean {
        return false
    }

    override fun remove() {}
    override fun isDead(): Boolean {
        return false
    }

    override fun isValid(): Boolean {
        return false
    }

    override fun sendMessage(message: String) {}
    override fun sendMessage(messages: Array<String>) {}
    override fun sendMessage(uuid: UUID?, s: String) {}
    override fun sendMessage(uuid: UUID?, vararg strings: String) {}
    override fun getServer(): Server {
        return null!!
    }

    override fun isPersistent(): Boolean {
        return false
    }

    override fun setPersistent(persistent: Boolean) {}
    override fun getPassenger(): Entity? {
        return null
    }

    override fun setPassenger(passenger: Entity): Boolean {
        return false
    }

    override fun getPassengers(): List<Entity> {
        return emptyList()
    }

    override fun addPassenger(passenger: Entity): Boolean {
        return false
    }

    override fun removePassenger(passenger: Entity): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun eject(): Boolean {
        return false
    }

    override fun getFallDistance(): Float {
        return 0F
    }

    override fun setFallDistance(distance: Float) {}
    override fun setLastDamageCause(event: EntityDamageEvent?) {}
    override fun getLastDamageCause(): EntityDamageEvent? {
        return null
    }

    override fun getUniqueId(): UUID {
        return UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

    override fun getPlayerProfile(): PlayerProfile {
        return null!!
    }

    override fun getTicksLived(): Int {
        return 0
    }

    override fun setTicksLived(value: Int) {}
    override fun playEffect(type: EntityEffect) {}
    override fun getType(): EntityType {
        return null!!
    }

    override fun isInsideVehicle(): Boolean {
        return false
    }

    override fun leaveVehicle(): Boolean {
        return false
    }

    override fun getVehicle(): Entity? {
        return null
    }

    override fun setCustomNameVisible(flag: Boolean) {}
    override fun isCustomNameVisible(): Boolean {
        return false
    }

    override fun setGlowing(flag: Boolean) {}
    override fun isGlowing(): Boolean {
        return false
    }

    override fun setInvulnerable(flag: Boolean) {}
    override fun isInvulnerable(): Boolean {
        return false
    }

    override fun isSilent(): Boolean {
        return false
    }

    override fun setSilent(flag: Boolean) {}
    override fun hasGravity(): Boolean {
        return false
    }

    override fun setGravity(gravity: Boolean) {}
    override fun getPortalCooldown(): Int {
        return 0
    }

    override fun setPortalCooldown(cooldown: Int) {}
    override fun getScoreboardTags(): Set<String> {
        return null!!
    }

    override fun addScoreboardTag(tag: String): Boolean {
        return false
    }

    override fun removeScoreboardTag(tag: String): Boolean {
        return false
    }

    override fun getPistonMoveReaction(): PistonMoveReaction {
        return null!!
    }

    override fun getFacing(): BlockFace {
        return null!!
    }

    override fun getPose(): Pose {
        return null!!
    }

    override fun getSpawnCategory(): SpawnCategory {
        return null!!
    }

    override fun isSneaking(): Boolean {
        return false
    }

    override fun setSneaking(sneak: Boolean) {}
    override fun isSprinting(): Boolean {
        return false
    }

    override fun setSprinting(sprinting: Boolean) {}
    override fun saveData() {}
    override fun loadData() {}
    override fun setSleepingIgnored(isSleeping: Boolean) {}
    override fun isSleepingIgnored(): Boolean {
        return false
    }

    override fun isOnline(): Boolean {
        return false
    }

    override fun isBanned(): Boolean {
        return false
    }

    override fun isWhitelisted(): Boolean {
        return false
    }

    override fun setWhitelisted(value: Boolean) {}
    override fun getPlayer(): Player? {
        return null
    }

    override fun getFirstPlayed(): Long {
        return 0
    }

    override fun getLastPlayed(): Long {
        return 0
    }

    override fun hasPlayedBefore(): Boolean {
        return false
    }

    override fun getBedSpawnLocation(): Location? {
        return null
    }

    @Throws(IllegalArgumentException::class)
    override fun incrementStatistic(statistic: Statistic) {
    }

    @Throws(IllegalArgumentException::class)
    override fun decrementStatistic(statistic: Statistic) {
    }

    @Throws(IllegalArgumentException::class)
    override fun incrementStatistic(statistic: Statistic, amount: Int) {
    }

    @Throws(IllegalArgumentException::class)
    override fun decrementStatistic(statistic: Statistic, amount: Int) {
    }

    @Throws(IllegalArgumentException::class)
    override fun setStatistic(statistic: Statistic, newValue: Int) {
    }

    @Throws(IllegalArgumentException::class)
    override fun getStatistic(statistic: Statistic): Int {
        return 0
    }

    @Throws(IllegalArgumentException::class)
    override fun incrementStatistic(statistic: Statistic, material: Material) {
    }

    @Throws(IllegalArgumentException::class)
    override fun decrementStatistic(statistic: Statistic, material: Material) {
    }

    @Throws(IllegalArgumentException::class)
    override fun getStatistic(statistic: Statistic, material: Material): Int {
        return 0
    }

    @Throws(IllegalArgumentException::class)
    override fun incrementStatistic(statistic: Statistic, material: Material, amount: Int) {
    }

    @Throws(IllegalArgumentException::class)
    override fun decrementStatistic(statistic: Statistic, material: Material, amount: Int) {
    }

    @Throws(IllegalArgumentException::class)
    override fun setStatistic(statistic: Statistic, material: Material, newValue: Int) {
    }

    @Throws(IllegalArgumentException::class)
    override fun incrementStatistic(statistic: Statistic, entityType: EntityType) {
    }

    @Throws(IllegalArgumentException::class)
    override fun decrementStatistic(statistic: Statistic, entityType: EntityType) {
    }

    @Throws(IllegalArgumentException::class)
    override fun getStatistic(statistic: Statistic, entityType: EntityType): Int {
        return 0
    }

    @Throws(IllegalArgumentException::class)
    override fun incrementStatistic(statistic: Statistic, entityType: EntityType, amount: Int) {
    }

    override fun decrementStatistic(statistic: Statistic, entityType: EntityType, amount: Int) {}
    override fun setStatistic(statistic: Statistic, entityType: EntityType, newValue: Int) {}
    override fun setBedSpawnLocation(location: Location?) {}
    override fun setBedSpawnLocation(location: Location?, force: Boolean) {}
    override fun playNote(loc: Location, instrument: Byte, note: Byte) {}
    override fun playNote(loc: Location, instrument: Instrument, note: Note) {}
    override fun playSound(location: Location, sound: Sound, volume: Float, pitch: Float) {}
    override fun playSound(location: Location, sound: String, volume: Float, pitch: Float) {}
    override fun playSound(location: Location, sound: Sound, category: SoundCategory, volume: Float, pitch: Float) {}
    override fun playSound(location: Location, sound: String, category: SoundCategory, volume: Float, pitch: Float) {}
    override fun playSound(entity: Entity, sound: Sound, v: Float, v1: Float) {}
    override fun playSound(entity: Entity, sound: Sound, soundCategory: SoundCategory, v: Float, v1: Float) {}
    override fun stopSound(sound: Sound) {}
    override fun stopSound(sound: String) {}
    override fun stopSound(sound: Sound, category: SoundCategory?) {}
    override fun stopSound(sound: String, category: SoundCategory?) {}
    override fun stopAllSounds() {}
    override fun playEffect(loc: Location, effect: Effect, data: Int) {}
    override fun <T> playEffect(loc: Location, effect: Effect, data: T?) {}
    override fun breakBlock(block: Block): Boolean {
        return false
    }

    override fun sendBlockChange(loc: Location, material: Material, data: Byte) {}
    override fun sendBlockChange(loc: Location, block: BlockData) {}
    override fun sendBlockDamage(location: Location, v: Float) {}
    override fun sendEquipmentChange(livingEntity: LivingEntity, equipmentSlot: EquipmentSlot, itemStack: ItemStack) {}

    @Throws(IllegalArgumentException::class)
    override fun sendSignChange(loc: Location, lines: Array<String>) {
    }

    @Throws(IllegalArgumentException::class)
    override fun sendSignChange(loc: Location, lines: Array<String>, dyeColor: DyeColor) {
    }

    @Throws(IllegalArgumentException::class)
    override fun sendSignChange(location: Location, strings: Array<String>, dyeColor: DyeColor, b: Boolean) {
    }

    override fun sendMap(map: MapView) {}
    override fun updateInventory() {}
    override fun getPreviousGameMode(): GameMode? {
        return null
    }

    override fun setPlayerTime(time: Long, relative: Boolean) {}
    override fun getPlayerTime(): Long {
        return 0
    }

    override fun getPlayerTimeOffset(): Long {
        return 0
    }

    override fun isPlayerTimeRelative(): Boolean {
        return false
    }

    override fun resetPlayerTime() {}
    override fun setPlayerWeather(type: WeatherType) {}
    override fun getPlayerWeather(): WeatherType? {
        return null
    }

    override fun resetPlayerWeather() {}
    override fun giveExp(amount: Int) {}
    override fun giveExpLevels(amount: Int) {}
    override fun getExp(): Float {
        return 0f
    }

    override fun setExp(exp: Float) {}
    override fun getLevel(): Int {
        return 0
    }

    override fun setLevel(level: Int) {}
    override fun getTotalExperience(): Int {
        return 0
    }

    override fun setTotalExperience(exp: Int) {}
    override fun sendExperienceChange(progress: Float) {}
    override fun sendExperienceChange(progress: Float, level: Int) {}
    override fun getExhaustion(): Float {
        return 0f
    }

    override fun setExhaustion(value: Float) {}
    override fun getSaturation(): Float {
        return 0f
    }

    override fun setSaturation(value: Float) {}
    override fun getFoodLevel(): Int {
        return 0
    }

    override fun setFoodLevel(value: Int) {}
    override fun getSaturatedRegenRate(): Int {
        return 0
    }

    override fun setSaturatedRegenRate(i: Int) {}
    override fun getUnsaturatedRegenRate(): Int {
        return 0
    }

    override fun setUnsaturatedRegenRate(i: Int) {}
    override fun getStarvationRate(): Int {
        return 0
    }

    override fun setStarvationRate(i: Int) {}
    override fun getAllowFlight(): Boolean {
        return false
    }

    override fun setAllowFlight(flight: Boolean) {}
    override fun hidePlayer(player: Player) {}
    override fun hidePlayer(plugin: Plugin, player: Player) {}
    override fun showPlayer(player: Player) {}
    override fun showPlayer(plugin: Plugin, player: Player) {}
    override fun canSee(player: Player): Boolean {
        return false
    }

    override fun hideEntity(plugin: Plugin, entity: Entity) {}
    override fun showEntity(plugin: Plugin, entity: Entity) {}
    override fun canSee(entity: Entity): Boolean {
        return false
    }

    override fun isFlying(): Boolean {
        return false
    }

    override fun setFlying(value: Boolean) {}

    @Throws(IllegalArgumentException::class)
    override fun setFlySpeed(value: Float) {
    }

    @Throws(IllegalArgumentException::class)
    override fun setWalkSpeed(value: Float) {
    }

    override fun getFlySpeed(): Float {
        return 0f
    }

    override fun getWalkSpeed(): Float {
        return 0f
    }

    override fun setTexturePack(url: String) {}
    override fun setResourcePack(url: String) {}
    override fun setResourcePack(url: String, hash: ByteArray) {}
    override fun setResourcePack(s: String, bytes: ByteArray, s1: String?) {}
    override fun setResourcePack(s: String, bytes: ByteArray, b: Boolean) {}
    override fun setResourcePack(s: String, bytes: ByteArray, s1: String?, b: Boolean) {}
    override fun getScoreboard(): Scoreboard {
        return null!!
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    override fun setScoreboard(scoreboard: Scoreboard) {
    }

    override fun isHealthScaled(): Boolean {
        return false
    }

    override fun setHealthScaled(scale: Boolean) {}

    @Throws(IllegalArgumentException::class)
    override fun setHealthScale(scale: Double) {
    }

    override fun getHealthScale(): Double {
        return 0.0
    }

    override fun getSpectatorTarget(): Entity? {
        return null
    }

    override fun setSpectatorTarget(entity: Entity?) {}
    override fun sendTitle(title: String?, subtitle: String?) {}
    override fun sendTitle(title: String?, subtitle: String?, fadeIn: Int, stay: Int, fadeOut: Int) {}
    override fun resetTitle() {}
    override fun spawnParticle(particle: Particle, location: Location, count: Int) {}
    override fun spawnParticle(particle: Particle, x: Double, y: Double, z: Double, count: Int) {}
    override fun <T> spawnParticle(particle: Particle, location: Location, count: Int, data: T?) {}
    override fun <T> spawnParticle(particle: Particle, x: Double, y: Double, z: Double, count: Int, data: T?) {}
    override fun spawnParticle(
        particle: Particle,
        location: Location,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double
    ) {
    }

    override fun spawnParticle(
        particle: Particle,
        x: Double,
        y: Double,
        z: Double,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double
    ) {
    }

    override fun <T> spawnParticle(
        particle: Particle,
        location: Location,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        data: T?
    ) {
    }

    override fun <T> spawnParticle(
        particle: Particle,
        x: Double,
        y: Double,
        z: Double,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        data: T?
    ) {
    }

    override fun spawnParticle(
        particle: Particle,
        location: Location,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        extra: Double
    ) {
    }

    override fun spawnParticle(
        particle: Particle,
        x: Double,
        y: Double,
        z: Double,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        extra: Double
    ) {
    }

    override fun <T> spawnParticle(
        particle: Particle,
        location: Location,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        extra: Double,
        data: T?
    ) {
    }

    override fun <T> spawnParticle(
        particle: Particle,
        x: Double,
        y: Double,
        z: Double,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        extra: Double,
        data: T?
    ) {
    }

    override fun getAdvancementProgress(advancement: Advancement): AdvancementProgress {
        return null!!
    }

    override fun getClientViewDistance(): Int {
        return 0
    }

    override fun getPing(): Int {
        return 0
    }

    override fun getLocale(): String {
        return null!!
    }

    override fun updateCommands() {}
    override fun openBook(book: ItemStack) {}
    override fun openSign(sign: Sign) {}
    override fun showDemoScreen() {}
    override fun isAllowingServerListings(): Boolean {
        return false
    }

    override fun spigot(): Player.Spigot {
        return null!!
    }

    override fun serialize(): Map<String, Any> {
        return null!!
    }

    override fun getName(): String {
        return null!!
    }

    override fun getInventory(): PlayerInventory {
        return null!!
    }

    override fun getEnderChest(): Inventory {
        TODO("Not yet implemented")
    }

    override fun getMainHand(): MainHand {
        return null!!
    }

    override fun setWindowProperty(prop: InventoryView.Property, value: Int): Boolean {
        return false
    }

    override fun getOpenInventory(): InventoryView {
        return null!!
    }

    override fun openInventory(inventory: Inventory): InventoryView? {
        return null
    }

    override fun openWorkbench(location: Location?, force: Boolean): InventoryView? {
        return null
    }

    override fun openEnchanting(location: Location?, force: Boolean): InventoryView? {
        return null
    }

    override fun openInventory(inventory: InventoryView) {}
    override fun openMerchant(trader: Villager, force: Boolean): InventoryView? {
        return null
    }

    override fun openMerchant(merchant: Merchant, force: Boolean): InventoryView? {
        return null
    }

    override fun closeInventory() {}
    override fun getItemInHand(): ItemStack {
        return null!!
    }

    override fun setItemInHand(item: ItemStack?) {}
    override fun getItemOnCursor(): ItemStack {
        return null!!
    }

    override fun setItemOnCursor(item: ItemStack?) {}
    override fun hasCooldown(material: Material): Boolean {
        return false
    }

    override fun getCooldown(material: Material): Int {
        return 0
    }

    override fun setCooldown(material: Material, ticks: Int) {}
    override fun getSleepTicks(): Int {
        return 0
    }

    override fun sleep(location: Location, force: Boolean): Boolean {
        return false
    }

    override fun wakeup(setSpawnLocation: Boolean) {}
    override fun getBedLocation(): Location {
        return null!!
    }

    override fun getGameMode(): GameMode {
        return null!!
    }

    override fun setGameMode(mode: GameMode) {}
    override fun isBlocking(): Boolean {
        return false
    }

    override fun isHandRaised(): Boolean {
        return false
    }

    override fun getItemInUse(): ItemStack? {
        return null
    }

    override fun getExpToLevel(): Int {
        return 0
    }

    override fun getAttackCooldown(): Float {
        return 0f
    }

    override fun discoverRecipe(recipe: NamespacedKey): Boolean {
        return false
    }

    override fun discoverRecipes(recipes: Collection<NamespacedKey>): Int {
        return 0
    }

    override fun undiscoverRecipe(recipe: NamespacedKey): Boolean {
        return false
    }

    override fun undiscoverRecipes(recipes: Collection<NamespacedKey>): Int {
        return 0
    }

    override fun hasDiscoveredRecipe(recipe: NamespacedKey): Boolean {
        return false
    }

    override fun getDiscoveredRecipes(): Set<NamespacedKey> {
        return null!!
    }

    override fun getShoulderEntityLeft(): Entity? {
        return null
    }

    override fun setShoulderEntityLeft(entity: Entity?) {}
    override fun getShoulderEntityRight(): Entity? {
        return null
    }

    override fun setShoulderEntityRight(entity: Entity?) {}
    override fun dropItem(b: Boolean): Boolean {
        return false
    }

    override fun getEyeHeight(): Double {
        return 0.0
    }

    override fun getEyeHeight(ignorePose: Boolean): Double {
        return 0.0
    }

    override fun getEyeLocation(): Location {
        return null!!
    }

    override fun getLineOfSight(transparent: Set<Material>?, maxDistance: Int): List<Block> {
        return null!!
    }

    override fun getTargetBlock(transparent: Set<Material>?, maxDistance: Int): Block {
        return null!!
    }

    override fun getLastTwoTargetBlocks(transparent: Set<Material>?, maxDistance: Int): List<Block> {
        return null!!
    }

    override fun getTargetBlockExact(maxDistance: Int): Block? {
        return null
    }

    override fun getTargetBlockExact(maxDistance: Int, fluidCollisionMode: FluidCollisionMode): Block? {
        return null
    }

    override fun rayTraceBlocks(maxDistance: Double): RayTraceResult? {
        return null
    }

    override fun rayTraceBlocks(maxDistance: Double, fluidCollisionMode: FluidCollisionMode): RayTraceResult? {
        return null
    }

    override fun getRemainingAir(): Int {
        return 0
    }

    override fun setRemainingAir(ticks: Int) {}
    override fun getMaximumAir(): Int {
        return 0
    }

    override fun setMaximumAir(ticks: Int) {}
    override fun getArrowCooldown(): Int {
        return 0
    }

    override fun setArrowCooldown(i: Int) {}
    override fun getArrowsInBody(): Int {
        return 0
    }

    override fun setArrowsInBody(i: Int) {}
    override fun getMaximumNoDamageTicks(): Int {
        return 0
    }

    override fun setMaximumNoDamageTicks(ticks: Int) {}
    override fun getLastDamage(): Double {
        return 0.0
    }

    override fun setLastDamage(damage: Double) {}
    override fun getNoDamageTicks(): Int {
        return 0
    }

    override fun setNoDamageTicks(ticks: Int) {}
    override fun getKiller(): Player? {
        return null
    }

    override fun addPotionEffect(effect: PotionEffect): Boolean {
        return false
    }

    override fun addPotionEffect(effect: PotionEffect, force: Boolean): Boolean {
        return false
    }

    override fun addPotionEffects(effects: Collection<PotionEffect>): Boolean {
        return false
    }

    override fun hasPotionEffect(type: PotionEffectType): Boolean {
        return false
    }

    override fun getPotionEffect(type: PotionEffectType): PotionEffect? {
        return null
    }

    override fun removePotionEffect(type: PotionEffectType) {}
    override fun getActivePotionEffects(): Collection<PotionEffect> {
        return null!!
    }

    override fun hasLineOfSight(other: Entity): Boolean {
        return false
    }

    override fun getRemoveWhenFarAway(): Boolean {
        return false
    }

    override fun setRemoveWhenFarAway(remove: Boolean) {}
    override fun getEquipment(): EntityEquipment? {
        return null
    }

    override fun setCanPickupItems(pickup: Boolean) {}
    override fun getCanPickupItems(): Boolean {
        return false
    }

    override fun isLeashed(): Boolean {
        return false
    }

    @Throws(IllegalStateException::class)
    override fun getLeashHolder(): Entity {
        return null!!
    }

    override fun setLeashHolder(holder: Entity?): Boolean {
        return false
    }

    override fun isGliding(): Boolean {
        return false
    }

    override fun setGliding(gliding: Boolean) {}
    override fun isSwimming(): Boolean {
        return false
    }

    override fun setSwimming(swimming: Boolean) {}
    override fun isRiptiding(): Boolean {
        return false
    }

    override fun isSleeping(): Boolean {
        return false
    }

    override fun isClimbing(): Boolean {
        return false
    }

    override fun setAI(ai: Boolean) {}
    override fun hasAI(): Boolean {
        return false
    }

    override fun attack(target: Entity) {}
    override fun swingMainHand() {}
    override fun swingOffHand() {}
    override fun setCollidable(collidable: Boolean) {}
    override fun isCollidable(): Boolean {
        return false
    }

    override fun getCollidableExemptions(): Set<UUID> {
        return null!!
    }

    override fun <T> getMemory(memoryKey: MemoryKey<T>): T? {
        return null
    }

    override fun <T> setMemory(memoryKey: MemoryKey<T>, memoryValue: T?) {}
    override fun getCategory(): EntityCategory {
        return null!!
    }

    override fun setInvisible(b: Boolean) {}
    override fun isInvisible(): Boolean {
        return false
    }

    override fun getAttribute(attribute: Attribute): AttributeInstance? {
        return null
    }

    override fun damage(amount: Double) {}
    override fun damage(amount: Double, source: Entity?) {}
    override fun getHealth(): Double {
        return 0.0
    }

    override fun setHealth(health: Double) {}
    override fun getAbsorptionAmount(): Double {
        return 0.0
    }

    override fun setAbsorptionAmount(amount: Double) {}
    override fun getMaxHealth(): Double {
        return 0.0
    }

    override fun setMaxHealth(health: Double) {}
    override fun resetMaxHealth() {}
    override fun getCustomName(): String? {
        return null
    }

    override fun setCustomName(name: String?) {}
    override fun setMetadata(metadataKey: String, newMetadataValue: MetadataValue) {}
    override fun getMetadata(metadataKey: String): List<MetadataValue> {
        return null!!
    }

    override fun hasMetadata(metadataKey: String): Boolean {
        return false
    }

    override fun removeMetadata(metadataKey: String, owningPlugin: Plugin) {}
    override fun isPermissionSet(name: String): Boolean {
        return false
    }

    override fun isPermissionSet(perm: Permission): Boolean {
        return false
    }

    override fun hasPermission(name: String): Boolean {
        return false
    }

    override fun hasPermission(perm: Permission): Boolean {
        return false
    }

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment {
        return null!!
    }

    override fun addAttachment(plugin: Plugin): PermissionAttachment {
        return null!!
    }

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean, ticks: Int): PermissionAttachment? {
        return null
    }

    override fun addAttachment(plugin: Plugin, ticks: Int): PermissionAttachment? {
        return null
    }

    override fun removeAttachment(attachment: PermissionAttachment) {}
    override fun recalculatePermissions() {}
    override fun getEffectivePermissions(): Set<PermissionAttachmentInfo> {
        return null!!
    }

    override fun isOp(): Boolean {
        return false
    }

    override fun setOp(value: Boolean) {}
    override fun getPersistentDataContainer(): PersistentDataContainer {
        return null!!
    }

    override fun sendPluginMessage(source: Plugin, channel: String, message: ByteArray) {}
    override fun getListeningPluginChannels(): Set<String> {
        return null!!
    }

    override fun <T : Projectile?> launchProjectile(projectile: Class<out T?>): T {
        return null!!
    }

    override fun <T : Projectile?> launchProjectile(projectile: Class<out T?>, velocity: Vector?): T {
        return null!!
    }
}
