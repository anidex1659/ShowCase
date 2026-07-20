package com.example.showcase.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


object VMocha {

    private var palette: ShowcasePalette = Themes.TokyoNight

    fun setTheme(theme: ShowcasePalette) {
        palette = theme
    }

    val Rosewater get() = palette.Rosewater
    val Flamingo get() = palette.Flamingo
    val Pink get() = palette.Pink
    val Mauve get() = palette.Mauve
    val Red get() = palette.Red
    val Maroon get() = palette.Maroon
    val Peach get() = palette.Peach
    val Yellow get() = palette.Yellow
    val Green get() = palette.Green
    val Teal get() = palette.Teal
    val Sky get() = palette.Sky
    val Sapphire get() = palette.Sapphire
    val Blue get() = palette.Blue
    val Lavender get() = palette.Lavender

    val Text get() = palette.Text
    val Subtext1 get() = palette.Subtext1
    val Subtext0 get() = palette.Subtext0

    val Overlay2 get() = palette.Overlay2
    val Overlay1 get() = palette.Overlay1
    val Overlay0 get() = palette.Overlay0

    val Surface2 get() = palette.Surface2
    val Surface1 get() = palette.Surface1
    val Surface0 get() = palette.Surface0

    val Base get() = palette.Base
    val Mantle get() = palette.Mantle
    val Crust get() = palette.Crust
}



object Themes {

    val Catppuccin = ShowcasePalette(

        Rosewater = Color(0xFFF5E0DC),
        Flamingo = Color(0xFFF2CDCD),
        Pink = Color(0xFFF5C2E7),
        Mauve = Color(0xFFCBA6F7),
        Red = Color(0xFFF38BA8),
        Maroon = Color(0xFFEBA0AC),
        Peach = Color(0xFFFAB387),
        Yellow = Color(0xFFF9E2AF),
        Green = Color(0xFFA6E3A1),
        Teal = Color(0xFF94E2D5),
        Sky = Color(0xFF89DCEB),
        Sapphire = Color(0xFF74C7EC),
        Blue = Color(0xFF89B4FA),
        Lavender = Color(0xFFB4BEFE),

        Text = Color(0xFFCDD6F4),
        Subtext1 = Color(0xFFBAC2DE),
        Subtext0 = Color(0xFFA6ADC8),

        Overlay2 = Color(0xFF9399B2),
        Overlay1 = Color(0xFF7F849C),
        Overlay0 = Color(0xFF6C7086),

        Surface2 = Color(0xFF585B70),
        Surface1 = Color(0xFF45475A),
        Surface0 = Color(0xFF313244),

        Base = Color(0xFF1E1E2E),
        Mantle = Color(0xFF181825),
        Crust = Color(0xFF11111B)
    )


    val CatppuccinLatte = ShowcasePalette(

        // Accent
        Rosewater = Color(0xFFDC8A78),
        Flamingo  = Color(0xFFDD7878),
        Pink      = Color(0xFFEA76CB),
        Mauve     = Color(0xFF8839EF),
        Red       = Color(0xFFD20F39),
        Maroon    = Color(0xFFE64553),
        Peach     = Color(0xFFFE640B),
        Yellow    = Color(0xFFDF8E1D),
        Green     = Color(0xFF40A02B),
        Teal      = Color(0xFF179299),
        Sky       = Color(0xFF04A5E5),
        Sapphire  = Color(0xFF209FB5),
        Blue      = Color(0xFF1E66F5),
        Lavender  = Color(0xFF7287FD),

        // Text
        Text      = Color(0xFF4C4F69),
        Subtext1  = Color(0xFF5C5F77),
        Subtext0  = Color(0xFF6C6F85),

        // Overlay
        Overlay2  = Color(0xFF7C7F93),
        Overlay1  = Color(0xFF8C8FA1),
        Overlay0  = Color(0xFF9CA0B0),

        // Surface
        Surface2  = Color(0xFFACB0BE),
        Surface1  = Color(0xFFBCC0CC),
        Surface0  = Color(0xFFCCD0DA),

        // Background
        Base      = Color(0xFFEFF1F5),
        Mantle    = Color(0xFFE6E9EF),
        Crust     = Color(0xFFDCE0E8)
    )

    val CatppuccinAMOLED = ShowcasePalette(

        // ===== Accents (unchanged) =====
        Rosewater = Color(0xFFF5E0DC),
        Flamingo = Color(0xFFF2CDCD),
        Pink = Color(0xFFF5C2E7),
        Mauve = Color(0xFFCBA6F7),
        Red = Color(0xFFF38BA8),
        Maroon = Color(0xFFEBA0AC),
        Peach = Color(0xFFFAB387),
        Yellow = Color(0xFFF9E2AF),
        Green = Color(0xFFA6E3A1),
        Teal = Color(0xFF94E2D5),
        Sky = Color(0xFF89DCEB),
        Sapphire = Color(0xFF74C7EC),
        Blue = Color(0xFF89B4FA),
        Lavender = Color(0xFFB4BEFE),

        // ===== Text =====
        Text = Color(0xFFF5F5F7),
        Subtext1 = Color(0xFFD0D0D4),
        Subtext0 = Color(0xFFA9A9B2),

        // ===== Overlays =====
        Overlay2 = Color(0xFF7A7A80),
        Overlay1 = Color(0xFF5E5E64),
        Overlay0 = Color(0xFF434349),

        // ===== Elevated Surfaces =====
        Surface2 = Color(0xFF2A2A2D),
        Surface1 = Color(0xFF1A1A1D),
        Surface0 = Color(0xFF111114),

        // ===== Backgrounds =====
        Base    = Color(0xFF000000), // App background
        Mantle  = Color(0xFF050505), // Cards
        Crust   = Color(0xFF090909)  // Sheets / Elevated panels
    )


    val TokyoNight = ShowcasePalette(

        // Accent
        Rosewater = Color(0xFFD5D6DB), // Light Gray
        Flamingo  = Color(0xFFC0CAF5), // Foreground
        Pink      = Color(0xFFBB9AF7),
        Mauve     = Color(0xFF9D7CD8),
        Red       = Color(0xFFF7768E),
        Maroon    = Color(0xFFFF9E64),
        Peach     = Color(0xFFFF9E64),
        Yellow    = Color(0xFFE0AF68),
        Green     = Color(0xFF9ECE6A),
        Teal      = Color(0xFF73DACA),
        Sky       = Color(0xFF7DCFFF),
        Sapphire  = Color(0xFF2AC3DE),
        Blue      = Color(0xFF7AA2F7),
        Lavender  = Color(0xFFBB9AF7),

        // Text
        Text      = Color(0xFFC0CAF5),
        Subtext1  = Color(0xFFA9B1D6),
        Subtext0  = Color(0xFF9AA5CE),

        // Overlay
        Overlay2  = Color(0xFF787C99),
        Overlay1  = Color(0xFF565F89),
        Overlay0  = Color(0xFF414868),

        // Surface
        Surface2  = Color(0xFF515C7E),
        Surface1  = Color(0xFF3B4261),
        Surface0  = Color(0xFF292E42),

        // Background
        Base      = Color(0xFF24283B),
        Mantle    = Color(0xFF1F2335),
        Crust     = Color(0xFF16161E)
    )
    val RosePine = ShowcasePalette(

        // Accent
        Rosewater = Color(0xFFEBBCBA), // Rose
        Flamingo  = Color(0xFFE0DEF4), // Text
        Pink      = Color(0xFFC4A7E7), // Iris
        Mauve     = Color(0xFFC4A7E7),
        Red       = Color(0xFFEB6F92), // Love
        Maroon    = Color(0xFFD7827E),
        Peach     = Color(0xFFF6C177), // Gold
        Yellow    = Color(0xFFF6C177),
        Green     = Color(0xFF9CCFD8), // Foam
        Teal      = Color(0xFF31748F), // Pine
        Sky       = Color(0xFF9CCFD8),
        Sapphire  = Color(0xFF31748F),
        Blue      = Color(0xFF31748F),
        Lavender  = Color(0xFFC4A7E7),

        // Text
        Text      = Color(0xFFE0DEF4),
        Subtext1  = Color(0xFF908CAA),
        Subtext0  = Color(0xFF6E6A86),

        // Overlay
        Overlay2  = Color(0xFF524F67),
        Overlay1  = Color(0xFF403D52),
        Overlay0  = Color(0xFF26233A),

        // Surface
        Surface2  = Color(0xFF403D52),
        Surface1  = Color(0xFF26233A),
        Surface0  = Color(0xFF1F1D2E),

        // Background
        Base      = Color(0xFF191724),
        Mantle    = Color(0xFF16141F),
        Crust     = Color(0xFF0F0D15)
    )
    val RosePineMoon = ShowcasePalette(

        Rosewater = Color(0xFFEA9A97),
        Flamingo  = Color(0xFFE0DEF4),
        Pink      = Color(0xFFC4A7E7),
        Mauve     = Color(0xFFC4A7E7),
        Red       = Color(0xFFEB6F92),
        Maroon    = Color(0xFFEA9A97),
        Peach     = Color(0xFFF6C177),
        Yellow    = Color(0xFFF6C177),
        Green     = Color(0xFF9CCFD8),
        Teal      = Color(0xFF3E8FB0),
        Sky       = Color(0xFF9CCFD8),
        Sapphire  = Color(0xFF3E8FB0),
        Blue      = Color(0xFF3E8FB0),
        Lavender  = Color(0xFFC4A7E7),

        Text      = Color(0xFFE0DEF4),
        Subtext1  = Color(0xFF908CAA),
        Subtext0  = Color(0xFF6E6A86),

        Overlay2  = Color(0xFF56526E),
        Overlay1  = Color(0xFF44415A),
        Overlay0  = Color(0xFF393552),

        Surface2  = Color(0xFF44415A),
        Surface1  = Color(0xFF393552),
        Surface0  = Color(0xFF2A273F),

        Base      = Color(0xFF232136),
        Mantle    = Color(0xFF1E1C2D),
        Crust     = Color(0xFF181725)
    )
    val RosePineDawn = ShowcasePalette(

        Rosewater = Color(0xFFD7827E),
        Flamingo  = Color(0xFF464261),
        Pink      = Color(0xFF907AA9),
        Mauve     = Color(0xFF907AA9),
        Red       = Color(0xFFB4637A),
        Maroon    = Color(0xFFD7827E),
        Peach     = Color(0xFFEA9D34),
        Yellow    = Color(0xFFEA9D34),
        Green     = Color(0xFF56949F),
        Teal      = Color(0xFF286983),
        Sky       = Color(0xFF56949F),
        Sapphire  = Color(0xFF286983),
        Blue      = Color(0xFF286983),
        Lavender  = Color(0xFF907AA9),

        Text      = Color(0xFF464261),
        Subtext1  = Color(0xFF797593),
        Subtext0  = Color(0xFF9893A5),

        Overlay2  = Color(0xFFCECACD),
        Overlay1  = Color(0xFFDFDAD9),
        Overlay0  = Color(0xFFF2E9E1),

        Surface2  = Color(0xFFDFDAD9),
        Surface1  = Color(0xFFF2E9E1),
        Surface0  = Color(0xFFFFFAF3),

        Base      = Color(0xFFFAF4ED),
        Mantle    = Color(0xFFF4EDE8),
        Crust     = Color(0xFFEDE5DE)
    )

    val AmoledBlack = ShowcasePalette(

        // Accent
        Rosewater = Color(0xFFFFD6E0),
        Flamingo  = Color(0xFFFFA3B5),
        Pink      = Color(0xFFFF4D8D),
        Mauve     = Color(0xFFB388FF),
        Red       = Color(0xFFFF3B30),
        Maroon    = Color(0xFFFF6B6B),
        Peach     = Color(0xFFFF9F43),
        Yellow    = Color(0xFFFFD60A),
        Green     = Color(0xFF30D158),
        Teal      = Color(0xFF64D2FF),
        Sky       = Color(0xFF5AC8FA),
        Sapphire  = Color(0xFF0A84FF),
        Blue      = Color(0xFF3EA6FF),
        Lavender  = Color(0xFF7D7AFF),

        // Text
        Text      = Color(0xFFFFFFFF),
        Subtext1  = Color(0xFFD0D0D0),
        Subtext0  = Color(0xFFA0A0A0),

        // Overlay
        Overlay2  = Color(0xFF707070),
        Overlay1  = Color(0xFF505050),
        Overlay0  = Color(0xFF303030),

        // Surface
        Surface2  = Color(0xFF242424),
        Surface1  = Color(0xFF181818),
        Surface0  = Color(0xFF101010),

        // Background
        Base      = Color(0xFF000000),
        Mantle    = Color(0xFF050505),
        Crust     = Color(0xFF0A0A0A)
    )

}