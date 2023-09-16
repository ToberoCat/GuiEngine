package io.github.toberocat.guiengine.exception

import io.github.toberocat.toberocore.action.Action

class GuiActionException(action: Action, cause: String) :
    GuiException("Action '${action.label()}' couldn't be executed. It's been caused by $cause")