package ca.on.hojat.gamenews.shared.core.device_info.model.screen

enum class ScreenDensity(
    val dpi: Int,
    val group: ScreenDensityGroup
) {

    UNDEFINED(
        dpi = -1,
        group = ScreenDensityGroup.UNDEFINED
    ),

    LOW(
        dpi = 120,
        group = ScreenDensityGroup.LOW
    ),
    ONE_FORTY(
        dpi = 140,
        group = ScreenDensityGroup.LOW
    ),
    MEDIUM(
        dpi = 160,
        group = ScreenDensityGroup.MEDIUM
    ),
    ONE_EIGHTY(
        dpi = 180,
        group = ScreenDensityGroup.MEDIUM
    ),
    TWO_HUNDRED(
        dpi = 200,
        group = ScreenDensityGroup.MEDIUM
    ),
    TV(
        dpi = 213,
        group = ScreenDensityGroup.TV
    ),
    TWO_TWENTY(
        dpi = 220,
        group = ScreenDensityGroup.TV
    ),
    HIGH(
        dpi = 240,
        group = ScreenDensityGroup.HIGH
    ),
    TWO_SIXTY(
        dpi = 260,
        group = ScreenDensityGroup.HIGH
    ),
    TWO_EIGHTY(
        dpi = 280,
        group = ScreenDensityGroup.HIGH
    ),
    THREE_HUNDRED(
        dpi = 300,
        group = ScreenDensityGroup.HIGH
    ),
    XHIGH(
        dpi = 320,
        group = ScreenDensityGroup.XHIGH
    ),
    THREE_FORTY(
        dpi = 340,
        group = ScreenDensityGroup.XHIGH
    ),
    THREE_SIXTY(
        dpi = 360,
        group = ScreenDensityGroup.XHIGH
    ),
    FOUR_HUNDRED(
        dpi = 400,
        group = ScreenDensityGroup.XHIGH
    ),
    FOUR_TWENTY(
        dpi = 420,
        group = ScreenDensityGroup.XHIGH
    ),
    FOUR_FORTY(
        dpi = 440,
        group = ScreenDensityGroup.XHIGH
    ),
    FOUR_FIFTY(
        dpi = 450,
        group = ScreenDensityGroup.XHIGH
    ),
    XXHIGH(
        dpi = 480,
        group = ScreenDensityGroup.XXHIGH
    ),
    FIVE_SIXTY(
        dpi = 560,
        group = ScreenDensityGroup.XXHIGH
    ),
    SIX_HUNDRED(
        dpi = 600,
        group = ScreenDensityGroup.XXHIGH
    ),
    XXXHIGH(
        dpi = 640,
        group = ScreenDensityGroup.XXXHIGH
    );

    companion object {

        @JvmName("forDpi")
        @JvmStatic
        internal fun Int.asScreenDensity(): ScreenDensity {
            return values().find { it.dpi == this }
                ?: UNDEFINED
        }
    }
}