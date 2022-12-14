package ca.on.hojat.gamenews.core.common_ui.base.events

sealed class GeneralCommand : Command {
    class ShowShortToast(val message: String) : GeneralCommand()
    class ShowLongToast(val message: String) : GeneralCommand()
}
