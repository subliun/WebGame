package entities

import common.WebSocket
import common.entities.PlayerInfo

class Player(val conn: WebSocket, val info: PlayerInfo)