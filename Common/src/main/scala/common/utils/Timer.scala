package common.utils

class Timer(delay: Long) {
  var lastResetTime: Long = 0

  def finished(): Boolean = timeSinceReset() >= delay
  def timeSinceReset(): Long = System.currentTimeMillis() - lastResetTime
  def reset(): Unit = {
    lastResetTime = System.currentTimeMillis()
  }
}
