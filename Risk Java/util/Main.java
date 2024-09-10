package util;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;

import participant.AsherLogic;
import participant.StupidLogic;

public class Main {

    public static void main(String[] args){
        System.out.println("test");

        // StupidLogic sl1 = new StupidLogic();
        // StupidLogic sl2 = new StupidLogic();
        // StupidLogic sl3 = new StupidLogic();
        AsherLogic sl1 = new AsherLogic();
        AsherLogic sl2 = new AsherLogic();
        AsherLogic sl3 = new AsherLogic();
        AsherLogic sl4 = new AsherLogic();

        Player one = new Player("playerOne", sl1);
        one.setColors(new Color(252, 186, 3), new Color(128, 252, 3));
        Player two = new Player("playerTwo", sl2);
        two.setColors(new Color(15, 252, 56), new Color(100, 160, 30));
        Player three = new Player("playerThree", sl3);
        three.setColors(new Color(0, 40, 160), new Color(25, 65, 200));
        Player four = new Player("ASHER", sl4);
        four.setColors(new Color(179, 137, 179), new Color(179, 137, 179));
        Player[] players = {one, two, three, four};
        Province Alaska = new Province("Alaska", one, 0);
        Province Northwest_Territory = new Province("Northwest_Territory", one, 0);
        Province Alberta = new Province("Alberta", one, 0);
        Province Ontario = new Province("Ontario", one, 0);
        Province Eastern_Canada = new Province("Eastern_Canada", one, 0);
        Province Greenland = new Province("Greenland", one, 0);
        Province Central_America = new Province("Central_America", one, 0);
        Province Western_United_States = new Province("Western_United_States", one, 0);
        Province Eastern_United_States = new Province("Eastern_United_States", one, 0);
        Province Venezuela = new Province("Venezuela", one, 0);
        Province Peru = new Province("Peru", one, 0);
        Province Brazil = new Province("Brazil", one, 0);
        Province Argentina = new Province("Argentina", one, 0);
        Province Iceland = new Province("Iceland", one, 0);
        Province United_Kingdom = new Province("United_Kingdom", one, 0);
        Province Scandinavia = new Province("Scandinavia", one, 0);
        Province Russia = new Province("Russia", one, 0);
        Province Northern_Europe = new Province("Northern_Europe", one, 0);
        Province Western_Europe = new Province("Western_Europe", one, 0);
        Province Southern_Europe = new Province("Southern_Europe", one, 0);
        Province West_Africa = new Province("West_Africa", one, 0);
        Province Egypt = new Province("Egypt", one, 0);
        Province East_Africa = new Province("East_Africa", one, 0);
        Province Central_Africa = new Province("Central_Africa", one, 0);
        Province South_Africa = new Province("South_Africa", one, 0);
        Province Madagascar = new Province("Madagascar", one, 0);
        Province Middle_East = new Province("Middle_East", one, 0);
        Province Ural = new Province("Ural", one, 0);
        Province Afghanistan = new Province("Afghanistan", one, 0);
        Province India = new Province("India", one, 0);
        Province China = new Province("China", one, 0);
        Province Southeast_Asia = new Province("Southeast_Asia", one, 0);
        Province Siberia = new Province("Siberia", one, 0);
        Province Yakutsk = new Province("Yakutsk", one, 0);
        Province Kamchatka = new Province("Kamchatka", one, 0);
        Province Irkutsk = new Province("Irkutsk", one, 0);
        Province Mongolia = new Province("Mongolia", one, 0);
        Province Japan = new Province("Japan", one, 0);
        Province Eastern_Australia = new Province("Eastern_Australia", one, 0);
        Province Western_Australia = new Province("Western_Australia", one, 0);
        Province New_Guinea = new Province("New_Guinea", one, 0);
        Province Indonesia = new Province("Indonesia", one, 0);
        int[] alaska = {40, 203, 42, 199, 47, 195, 51, 194, 54, 190, 55, 182, 49, 180, 44, 180, 43, 173, 35, 170, 35, 166, 33, 160, 38, 153, 57, 154, 58, 147, 45, 146, 43, 144, 46, 135, 52, 136, 57, 139, 64, 135, 66, 132, 61, 130, 59, 122, 54, 121, 58, 117, 70, 110, 78, 104, 83, 104, 84, 111, 90, 110, 93, 114, 99, 116, 110, 117, 116, 119, 115, 130, 116, 142, 116, 150, 115, 158, 115, 172, 117, 175, 128, 174, 131, 182, 133, 197, 141, 203, 138, 210, 134, 214, 129, 202, 125, 192, 118, 186, 110, 179, 102, 177, 93, 178, 84, 183, 86, 172, 81, 170, 73, 174, 61, 188, 57, 194, 51, 198, 48, 202, 44, 203};
        int[] northwestCan = {118, 170, 118, 162, 119, 151, 119, 142, 119, 133, 119, 126, 120, 119, 128, 120, 137, 121, 148, 121, 158, 120, 165, 120, 170, 115, 172, 115, 180, 112, 181, 113, 179, 119, 188, 118, 195, 122, 198, 126, 204, 123, 203, 131, 212, 132, 219, 132, 229, 132, 230, 131, 230, 124, 236, 127, 244, 128, 245, 128, 248, 127, 251, 126, 253, 123, 253, 120, 255, 119, 258, 126, 266, 126, 266, 122, 266, 119, 267, 112, 268, 110, 269, 102, 272, 98, 274, 94, 278, 90, 284, 90, 278, 97, 274, 102, 274, 108, 277, 115, 278, 120, 281, 125, 282, 125, 285, 123, 286, 126, 294, 130, 298, 129, 299, 123, 302, 133, 290, 142, 284, 147, 279, 156, 277, 157, 267, 162, 264, 170, 260, 172, 257, 176, 257, 178, 254, 172, 240, 170, 232, 170, 226, 170, 209, 171, 190, 171, 170, 170, 158, 168, 142, 170, 120, 168};
        int[] alberta = {134, 220, 138, 214, 140, 211, 143, 203, 143, 201, 138, 198, 135, 197, 136, 187, 136, 182, 135, 180, 132, 174, 141, 174, 146, 175, 152, 175, 166, 174, 175, 175, 186, 176, 226, 177, 226, 189, 225, 196, 226, 207, 226, 223, 225, 238, 225, 250, 225, 252, 213, 251, 202, 250, 186, 250, 176, 250, 165, 249, 157, 249, 151, 249, 150, 249, 146, 242, 143, 234, 138, 226, 136, 220};
        int[] ontario = {229, 251, 230, 175, 251, 176, 253, 180, 253, 193, 254, 197, 259, 204, 265, 210, 272, 215, 284, 218, 292, 221, 290, 230, 294, 235, 298, 239, 298, 258, 297, 270, 298, 274, 306, 272, 312, 276, 305, 278, 300, 278, 298, 281, 296, 285, 293, 286, 291, 286, 289, 289, 285, 288, 288, 281, 293, 281, 295, 281, 295, 274, 290, 268, 287, 266, 284, 263, 283, 258, 279, 254, 275, 250, 272, 248, 270, 249, 270, 253, 263, 256, 260, 254, 252, 254, 250, 254, 250, 250, 229, 252};
        int[] easternCan = {300, 270, 301, 241, 304, 234, 306, 219, 310, 218, 317, 208, 318, 204, 318, 200, 313, 199, 312, 188, 318, 182, 320, 182, 320, 172, 323, 174, 329, 175, 331, 175, 333, 172, 334, 172, 336, 182, 341, 182, 340, 192, 353, 198, 356, 194, 356, 190, 359, 188, 360, 194, 365, 201, 366, 209, 371, 217, 377, 221, 385, 226, 384, 236, 373, 243, 358, 251, 354, 248, 335, 257, 346, 260, 350, 255, 355, 266, 354, 269, 354, 278, 362, 277, 364, 275, 358, 282, 356, 284, 354, 287, 354, 291, 350, 290, 349, 281, 349, 273, 348, 265, 344, 263, 340, 262, 336, 262, 334, 269, 334, 273, 315, 275, 310, 269, 299, 270, 301, 256};
        int[] greenland = {399, 197, 400, 185, 403, 178, 406, 171, 409, 162, 416, 160, 422, 155, 431, 150, 440, 144, 450, 137, 448, 131, 448, 124, 451, 124, 458, 131, 461, 130, 453, 118, 453, 109, 460, 114, 469, 103, 463, 99, 466, 94, 466, 89, 462, 89, 460, 86, 461, 79, 461, 75, 473, 59, 455, 62, 439, 64, 437, 61, 448, 58, 453, 57, 453, 54, 449, 56, 446, 52, 410, 45, 402, 50, 392, 52, 390, 59, 383, 62, 380, 63, 378, 66, 358, 69, 346, 77, 339, 82, 343, 86, 339, 92, 336, 94, 330, 93, 328, 98, 326, 102, 326, 106, 330, 107, 338, 107, 343, 106, 348, 103, 352, 106, 362, 108, 365, 109, 366, 114, 364, 118, 369, 130, 370, 133, 375, 133, 372, 142, 376, 148, 374, 159, 378, 169, 379, 180, 382, 186, 387, 192, 391, 198, 399, 198};
        int[] westernUS = {151, 254, 155, 254, 160, 254, 165, 254, 180, 256, 202, 256, 210, 255, 226, 256, 241, 255, 246, 256, 246, 262, 246, 269, 246, 282, 246, 289, 246, 298, 246, 309, 246, 311, 230, 312, 228, 312, 226, 318, 227, 325, 222, 325, 218, 326, 218, 330, 216, 336, 216, 338, 216, 345, 208, 346, 206, 348, 208, 352, 213, 358, 210, 361, 204, 359, 201, 354, 195, 348, 184, 349, 175, 349, 170, 342, 153, 342, 149, 329, 146, 323, 146, 308, 143, 298, 147, 288, 150, 278, 150, 268, 152, 263, 152, 254};
        int[] easternUS = {250, 257, 256, 256, 254, 263, 254, 266, 261, 266, 266, 268, 272, 272, 269, 277, 267, 284, 267, 292, 269, 297, 272, 297, 277, 291, 277, 282, 279, 280, 279, 290, 278, 297, 286, 299, 295, 298, 295, 293, 302, 292, 314, 279, 320, 278, 324, 279, 336, 275, 337, 267, 341, 266, 346, 270, 346, 277, 330, 294, 329, 297, 331, 303, 322, 310, 311, 318, 311, 319, 310, 326, 317, 332, 309, 338, 299, 347, 291, 356, 291, 358, 296, 364, 296, 373, 298, 379, 290, 384, 288, 383, 288, 374, 290, 371, 290, 363, 287, 359, 284, 358, 274, 361, 265, 355, 252, 358, 232, 358, 225, 362, 218, 370, 213, 363, 216, 358, 210, 350, 218, 347, 222, 330, 231, 328, 232, 316, 250, 314, 250, 306, 250, 296, 250, 286, 251, 277, 251, 265, 250, 257};
        int[] centralamer = {156, 349, 168, 348, 174, 354, 193, 354, 202, 365, 208, 364, 215, 373, 212, 378, 211, 384, 211, 392, 212, 399, 217, 406, 218, 408, 225, 406, 230, 403, 235, 399, 238, 397, 247, 398, 238, 410, 234, 418, 234, 418, 243, 426, 241, 431, 232, 450, 234, 453, 245, 450, 249, 450, 251, 455, 246, 461, 240, 458, 230, 454, 229, 452, 229, 449, 226, 440, 225, 434, 221, 426, 218, 422, 218, 420, 205, 416, 198, 414, 193, 410, 190, 406, 191, 398, 191, 394, 186, 390, 181, 391, 177, 391, 179, 398, 181, 402, 179, 402, 176, 397, 174, 392, 170, 383, 165, 374, 166, 370, 163, 364, 159, 357, 155, 348};
        int[] venezuela = {254, 456, 260, 450, 269, 447, 273, 441, 286, 438, 286, 448, 288, 454, 290, 454, 294, 445, 301, 444, 307, 447, 313, 446, 315, 446, 319, 443, 325, 446, 326, 453, 336, 458, 341, 462, 346, 467, 349, 467, 350, 464, 356, 466, 362, 470, 371, 473, 364, 478, 357, 480, 350, 485, 334, 484, 334, 480, 330, 471, 326, 470, 321, 474, 312, 476, 310, 475, 308, 482, 303, 486, 299, 483, 290, 484, 286, 486, 284, 490, 283, 498, 283, 505, 279, 506, 275, 501, 270, 498, 264, 499, 258, 497, 244, 487, 249, 480, 249, 476, 249, 470, 249, 466};
        int[] peru = {241, 490, 250, 499, 255, 502, 260, 504, 263, 504, 268, 502, 275, 507, 274, 510, 267, 513, 259, 524, 259, 527, 266, 530, 273, 538, 282, 534, 284, 535, 278, 542, 279, 545, 282, 546, 288, 546, 290, 546, 296, 543, 302, 540, 306, 539, 308, 543, 313, 546, 323, 554, 326, 554, 328, 551, 334, 557, 335, 559, 333, 562, 332, 564, 340, 566, 345, 576, 346, 580, 344, 584, 345, 590, 350, 593, 357, 595, 360, 606, 352, 618, 348, 617, 346, 605, 339, 602, 334, 598, 332, 598, 328, 590, 326, 592, 314, 592, 313, 589, 310, 588, 307, 589, 306, 589, 305, 595, 302, 596, 297, 586, 293, 583, 293, 575, 287, 575, 285, 578, 276, 571, 267, 566, 258, 556, 247, 542, 239, 531, 234, 524, 236, 516, 235, 503, 241, 490};
        int[] brazil = {374, 475, 380, 486, 376, 493, 379, 496, 386, 494, 391, 491, 442, 514, 445, 530, 430, 543, 425, 567, 427, 574, 414, 595, 394, 599, 386, 604, 383, 611, 382, 618, 385, 627, 380, 627, 368, 642, 361, 635, 362, 628, 362, 621, 366, 612, 360, 593, 358, 591, 354, 590, 348, 588, 346, 585, 350, 580, 342, 563, 341, 562, 338, 562, 338, 556, 328, 546, 324, 550, 311, 543, 308, 537, 304, 535, 295, 538, 288, 543, 282, 542, 286, 534, 286, 531, 285, 530, 273, 532, 263, 526, 271, 513, 277, 509, 286, 509, 289, 501, 286, 493, 292, 487, 298, 487, 303, 493, 311, 486, 313, 478, 316, 479, 328, 474, 332, 487, 354, 488, 358, 484, 365, 484, 374, 474};
        int[] argentina = {286, 582, 288, 579, 291, 579, 290, 583, 292, 586, 297, 591, 298, 595, 299, 599, 308, 598, 307, 593, 310, 593, 312, 597, 326, 596, 328, 594, 330, 598, 344, 606, 343, 619, 355, 621, 360, 612, 363, 612, 360, 619, 355, 623, 352, 626, 351, 630, 358, 629, 358, 635, 364, 638, 366, 644, 368, 645, 354, 650, 351, 649, 345, 648, 353, 666, 345, 671, 342, 667, 332, 673, 333, 677, 328, 683, 325, 690, 321, 699, 317, 724, 342, 764, 333, 769, 326, 762, 314, 759, 314, 754, 311, 753, 296, 750, 294, 747, 291, 743, 290, 739, 287, 734, 286, 728, 284, 722, 284, 715, 285, 706, 285, 697, 285, 690, 282, 682, 282, 670, 285, 662, 285, 654, 284, 646, 286, 638, 289, 630, 289, 614, 289, 606, 291, 600, 290, 593, 289, 586, 287, 582, 289, 578};
        int[] iceland = {520, 167, 524, 175, 533, 182, 532, 189, 526, 196, 525, 198, 518, 199, 510, 210, 499, 208, 479, 205, 483, 198, 483, 194, 474, 193, 474, 190, 483, 183, 482, 181, 473, 181, 472, 178, 478, 172, 485, 171, 487, 181, 492, 180, 494, 175, 501, 175, 511, 170, 513, 166, 520, 166};
        int[] uk = {474, 222, 489, 222, 484, 228, 482, 234, 494, 234, 491, 246, 483, 253, 495, 255, 499, 270, 506, 276, 510, 285, 503, 287, 518, 292, 510, 299, 517, 310, 513, 314, 501, 312, 489, 313, 487, 316, 482, 315, 477, 319, 479, 310, 490, 307, 490, 302, 484, 304, 474, 301, 466, 301, 460, 299, 454, 304, 442, 306, 442, 298, 446, 297, 451, 291, 444, 287, 447, 283, 445, 278, 451, 280, 456, 277, 455, 270, 463, 268, 466, 271, 470, 270, 472, 278, 467, 283, 471, 290, 469, 297, 476, 299, 481, 295, 482, 290, 477, 286, 477, 283, 484, 285, 486, 282, 484, 271, 477, 270, 475, 266, 478, 262, 478, 258, 472, 254, 474, 247, 469, 249, 472, 243, 472, 240, 466, 239, 465, 237, 475, 223};
        int[] scandinavia = {644, 127, 639, 146, 642, 182, 646, 186, 638, 203, 630, 204, 626, 209, 615, 192, 618, 189, 621, 182, 617, 174, 605, 185, 603, 193, 600, 199, 602, 204, 605, 207, 606, 218, 600, 216, 599, 222, 594, 230, 594, 239, 589, 246, 581, 243, 574, 223, 568, 218, 564, 225, 558, 229, 555, 226, 552, 218, 550, 192, 562, 184, 584, 149, 590, 146, 587, 142, 591, 139, 619, 126, 643, 127};
        int[] russia = {647, 131, 680, 134, 685, 139, 686, 144, 682, 148, 663, 151, 646, 148, 654, 157, 663, 161, 666, 170, 678, 169, 673, 161, 676, 158, 682, 160, 685, 167, 689, 165, 689, 157, 699, 153, 698, 147, 694, 146, 695, 135, 704, 142, 706, 150, 725, 141, 728, 132, 738, 128, 744, 134, 755, 130, 758, 123, 779, 134, 777, 146, 769, 158, 768, 177, 767, 185, 771, 192, 766, 202, 774, 202, 769, 219, 781, 230, 772, 241, 776, 252, 746, 260, 722, 273, 718, 290, 727, 309, 726, 318, 716, 327, 726, 350, 735, 354, 730, 358, 728, 371, 702, 378, 706, 372, 704, 364, 685, 354, 682, 349, 677, 349, 690, 334, 691, 326, 682, 329, 677, 334, 670, 336, 666, 342, 670, 345, 667, 352, 657, 348, 658, 341, 650, 338, 644, 350, 640, 347, 638, 320, 631, 318, 629, 311, 618, 311, 625, 302, 634, 302, 634, 297, 640, 286, 632, 254, 625, 254, 620, 251, 615, 241, 622, 234, 622, 227, 631, 233, 634, 216, 626, 221, 621, 221, 626, 214, 640, 211, 645, 215, 649, 210, 643, 202, 648, 186, 646, 180, 642, 149, 647, 130};
        int[] northeu = {565, 262, 564, 242, 574, 238, 575, 248, 571, 253, 572, 259, 577, 254, 590, 254, 596, 254, 609, 250, 618, 254, 623, 258, 630, 258, 631, 281, 638, 287, 623, 300, 617, 311, 626, 312, 613, 326, 613, 338, 599, 338, 598, 328, 602, 323, 598, 319, 590, 325, 578, 323, 578, 333, 557, 332, 550, 336, 544, 322, 538, 322, 533, 313, 540, 308, 540, 296, 546, 293, 554, 282, 558, 271, 566, 262};
        int[] westeu = {531, 314, 536, 322, 543, 322, 550, 338, 544, 342, 546, 354, 538, 355, 543, 362, 549, 357, 550, 363, 545, 368, 550, 374, 546, 379, 540, 378, 534, 373, 531, 373, 530, 384, 529, 406, 528, 414, 518, 414, 520, 422, 512, 426, 512, 434, 502, 437, 500, 428, 489, 422, 485, 427, 473, 418, 466, 418, 466, 410, 468, 402, 474, 396, 474, 390, 470, 384, 466, 381, 474, 370, 482, 376, 490, 373, 496, 374, 502, 372, 505, 366, 502, 366, 502, 358, 507, 356, 506, 351, 499, 353, 498, 347, 486, 338, 492, 334, 502, 341, 508, 326, 513, 331, 524, 325, 522, 320, 534, 314};
        int[] southeu = {626, 312, 628, 318, 635, 321, 638, 342, 637, 350, 642, 353, 631, 376, 628, 391, 618, 391, 622, 402, 618, 406, 626, 419, 624, 423, 618, 417, 610, 418, 606, 412, 608, 394, 601, 391, 602, 383, 587, 367, 577, 366, 576, 373, 596, 396, 592, 398, 585, 390, 582, 392, 586, 400, 584, 407, 578, 411, 572, 418, 564, 417, 557, 413, 574, 403, 574, 389, 568, 384, 558, 379, 557, 369, 547, 368, 552, 363, 552, 357, 546, 357, 550, 352, 551, 347, 547, 342, 558, 335, 562, 340, 579, 334, 580, 326, 589, 326, 598, 319, 602, 324, 597, 329, 598, 338, 614, 338, 613, 326, 628, 313};
        int[] westafr = {502, 441, 510, 436, 514, 440, 522, 434, 542, 433, 554, 422, 573, 420, 574, 430, 589, 442, 578, 448, 582, 454, 575, 461, 576, 466, 580, 470, 581, 486, 598, 493, 617, 495, 617, 501, 624, 502, 626, 507, 634, 507, 634, 520, 629, 532, 632, 546, 622, 556, 602, 563, 604, 590, 588, 586, 585, 591, 580, 590, 581, 579, 563, 567, 522, 573, 519, 577, 480, 532, 480, 526, 485, 519, 484, 490, 495, 466, 502, 438};
        int[] egypt = {591, 443, 605, 448, 606, 452, 611, 453, 614, 445, 628, 445, 633, 450, 641, 452, 663, 454, 670, 456, 674, 452, 678, 457, 677, 466, 666, 458, 665, 464, 674, 474, 676, 482, 681, 485, 682, 494, 640, 495, 639, 502, 627, 506, 626, 499, 618, 498, 618, 492, 598, 491, 582, 482, 582, 469, 578, 465, 578, 461, 585, 453, 582, 452, 592, 443};
        int[] eastafr = {641, 500, 688, 500, 696, 523, 702, 535, 714, 543, 709, 549, 720, 554, 728, 551, 730, 547, 745, 547, 746, 554, 742, 566, 735, 578, 698, 614, 694, 626, 694, 629, 696, 638, 692, 653, 680, 655, 680, 670, 673, 661, 677, 646, 667, 638, 662, 637, 659, 618, 665, 614, 667, 605, 673, 599, 679, 595, 679, 590, 683, 584, 666, 578, 662, 581, 658, 574, 654, 578, 638, 556, 633, 541, 631, 534, 634, 530, 637, 525, 638, 517, 635, 508, 642, 506, 641, 499};
        int[] centafr = {633, 551, 636, 561, 652, 581, 658, 577, 662, 585, 672, 582, 682, 584, 679, 594, 665, 602, 665, 612, 657, 618, 660, 638, 652, 644, 662, 651, 662, 656, 654, 658, 653, 653, 641, 650, 638, 647, 631, 649, 632, 638, 627, 631, 615, 633, 610, 625, 598, 620, 590, 622, 583, 614, 576, 608, 579, 591, 587, 593, 589, 588, 607, 590, 606, 566, 632, 548};
        int[] southafr = {591, 624, 598, 622, 609, 628, 614, 636, 626, 634, 631, 638, 629, 645, 630, 652, 636, 650, 642, 654, 650, 654, 651, 659, 661, 658, 662, 653, 662, 650, 653, 643, 659, 638, 667, 641, 674, 646, 672, 652, 682, 671, 682, 655, 690, 654, 698, 648, 703, 650, 703, 659, 698, 677, 694, 681, 688, 680, 676, 693, 683, 697, 683, 706, 679, 711, 673, 716, 670, 730, 664, 734, 658, 752, 615, 763, 611, 761, 615, 754, 600, 717, 605, 712, 588, 684, 590, 663, 598, 654, 591, 646, 596, 634, 593, 626};
        int[] mad = {755, 654, 758, 666, 763, 672, 761, 676, 754, 674, 752, 684, 755, 690, 733, 736, 711, 739, 709, 734, 714, 730, 707, 718, 718, 710, 723, 694, 716, 690, 722, 684, 734, 678, 737, 670, 742, 670, 746, 666, 752, 667, 752, 658, 758, 654};
        int[] mideast = {637, 371, 645, 381, 653, 381, 662, 374, 678, 374, 690, 384, 702, 381, 727, 375, 734, 376, 742, 382, 749, 382, 765, 372, 773, 374, 772, 381, 785, 385, 781, 397, 782, 405, 785, 418, 793, 428, 792, 442, 783, 453, 774, 456, 770, 445, 766, 453, 757, 450, 752, 442, 751, 428, 744, 429, 738, 436, 752, 466, 758, 467, 768, 461, 779, 468, 786, 480, 779, 488, 778, 499, 762, 518, 754, 527, 732, 530, 721, 533, 718, 517, 678, 467, 681, 457, 676, 450, 681, 442, 679, 415, 671, 411, 664, 418, 658, 410, 646, 418, 643, 412, 634, 414, 634, 403, 628, 400, 629, 392, 636, 371};
        int[] ural = {774, 97, 789, 104, 784, 114, 795, 128, 795, 134, 790, 143, 782, 156, 792, 153, 800, 145, 802, 137, 805, 131, 814, 147, 816, 157, 822, 158, 822, 174, 828, 177, 837, 191, 836, 203, 845, 214, 835, 241, 842, 238, 846, 240, 845, 251, 861, 274, 855, 276, 854, 281, 846, 299, 839, 298, 834, 287, 825, 283, 810, 286, 806, 276, 790, 264, 786, 251, 779, 251, 774, 239, 783, 228, 771, 218, 775, 201, 770, 201, 774, 192, 770, 183, 770, 158, 779, 150, 780, 134, 784, 129, 772, 118, 776, 98};
        int[] afga = {724, 275, 748, 262, 776, 254, 786, 256, 790, 269, 809, 288, 825, 286, 837, 292, 841, 302, 837, 309, 845, 315, 834, 334, 822, 333, 820, 346, 830, 361, 825, 370, 817, 363, 805, 363, 806, 370, 793, 370, 786, 383, 774, 378, 776, 370, 758, 370, 755, 362, 749, 362, 749, 357, 757, 355, 755, 346, 747, 347, 752, 338, 740, 332, 739, 322, 746, 318, 746, 303, 730, 306, 723, 291, 725, 274};
        int[] india = {795, 374, 806, 374, 808, 366, 815, 365, 820, 371, 826, 371, 830, 363, 849, 366, 850, 378, 844, 389, 866, 405, 883, 407, 886, 414, 906, 413, 907, 418, 895, 447, 888, 440, 881, 444, 888, 450, 886, 459, 876, 456, 870, 481, 863, 504, 859, 534, 855, 541, 851, 541, 846, 538, 828, 488, 829, 471, 833, 459, 828, 455, 824, 464, 818, 462, 802, 439, 793, 441, 795, 428, 787, 414, 790, 408, 786, 404, 786, 393, 789, 384, 795, 375};
        int[] china = {849, 302, 847, 293, 851, 283, 858, 283, 858, 277, 868, 283, 870, 277, 882, 284, 878, 293, 889, 298, 902, 326, 908, 326, 918, 338, 934, 334, 934, 339, 957, 340, 962, 346, 967, 340, 984, 342, 1002, 362, 999, 398, 993, 411, 982, 424, 975, 433, 975, 441, 968, 444, 968, 436, 962, 435, 954, 421, 946, 430, 936, 430, 936, 436, 930, 438, 915, 416, 907, 410, 887, 411, 885, 401, 866, 401, 854, 380, 852, 364, 842, 362, 838, 356, 832, 361, 827, 350, 826, 334, 833, 335, 847, 319, 840, 310, 841, 302, 850, 301};
        int[] southasia = {916, 418, 927, 439, 938, 439, 939, 431, 946, 430, 952, 426, 961, 436, 950, 446, 954, 457, 960, 457, 962, 467, 973, 486, 970, 504, 961, 518, 953, 498, 940, 489, 937, 502, 945, 510, 946, 527, 934, 522, 930, 488, 918, 476, 908, 478, 902, 451, 895, 451, 906, 421, 914, 417};
        int[] siberia = {814, 146, 820, 138, 814, 125, 801, 128, 796, 108, 802, 99, 807, 118, 818, 114, 807, 94, 811, 90, 831, 96, 818, 86, 824, 81, 833, 83, 829, 72, 839, 65, 860, 59, 868, 67, 872, 57, 877, 57, 879, 65, 893, 62, 892, 68, 872, 90, 881, 95, 886, 87, 902, 86, 910, 82, 926, 88, 930, 96, 926, 106, 928, 122, 911, 118, 906, 138, 914, 142, 915, 186, 918, 212, 902, 209, 882, 234, 880, 261, 888, 266, 886, 280, 891, 287, 889, 298, 883, 294, 882, 283, 872, 276, 866, 280, 862, 277, 855, 254, 847, 250, 846, 237, 836, 236, 847, 215, 838, 198, 840, 187, 830, 173, 824, 155, 814, 148, 818, 143};
        int[] yak = {921, 179, 914, 135, 906, 135, 912, 122, 921, 126, 929, 122, 932, 98, 927, 89, 933, 76, 942, 75, 941, 91, 949, 85, 967, 95, 967, 90, 962, 86, 967, 86, 975, 93, 982, 88, 978, 79, 988, 82, 992, 89, 998, 82, 1004, 82, 1003, 91, 1014, 90, 1014, 98, 999, 107, 998, 121, 1010, 126, 1009, 132, 1004, 132, 1006, 141, 991, 139, 990, 134, 981, 132, 970, 142, 966, 162, 942, 166, 938, 161, 932, 164, 934, 174, 926, 179, 921, 178};
        int[] kam = {1002, 110, 1016, 102, 1016, 94, 1024, 88, 1033, 99, 1055, 99, 1068, 102, 1061, 92, 1067, 90, 1100, 108, 1104, 117, 1116, 116, 1116, 131, 1109, 125, 1102, 125, 1100, 118, 1094, 118, 1094, 127, 1087, 128, 1089, 134, 1097, 140, 1098, 145, 1088, 146, 1083, 162, 1066, 163, 1066, 178, 1072, 196, 1068, 222, 1058, 214, 1054, 199, 1058, 170, 1067, 155, 1064, 146, 1057, 146, 1054, 163, 1047, 150, 1038, 154, 1038, 166, 1044, 173, 1032, 167, 1026, 173, 1004, 173, 993, 201, 1001, 203, 1006, 211, 1009, 204, 1017, 206, 1016, 211, 1025, 244, 1023, 267, 1020, 285, 1015, 284, 1010, 275, 998, 271, 1004, 266, 1002, 248, 1008, 233, 1005, 226, 998, 218, 980, 214, 966, 199, 967, 180, 964, 166, 975, 149, 980, 138, 987, 138, 1006, 146, 1008, 136, 1011, 132, 1013, 122, 1003, 122, 1003, 107};
        int[] irk = {902, 211, 906, 214, 914, 220, 918, 215, 917, 185, 926, 182, 936, 176, 936, 165, 939, 165, 942, 171, 947, 168, 947, 162, 962, 163, 961, 175, 965, 182, 962, 198, 979, 214, 979, 223, 986, 223, 988, 219, 996, 221, 996, 226, 1002, 226, 1001, 230, 1006, 234, 1000, 244, 1002, 255, 998, 259, 994, 256, 994, 250, 980, 242, 980, 234, 972, 233, 968, 237, 964, 233, 959, 233, 958, 238, 962, 246, 958, 250, 960, 260, 954, 266, 953, 259, 947, 260, 945, 264, 939, 267, 934, 264, 912, 262, 907, 266, 894, 260, 887, 262, 886, 254, 882, 257, 885, 231, 903, 211};
        int[] mong = {891, 263, 909, 269, 912, 265, 932, 267, 938, 271, 947, 266, 947, 262, 952, 262, 952, 268, 958, 266, 962, 259, 962, 252, 964, 247, 962, 239, 963, 237, 972, 236, 977, 236, 978, 246, 990, 253, 993, 260, 1000, 262, 1000, 265, 996, 267, 998, 274, 1008, 278, 1013, 286, 1006, 307, 1017, 318, 1022, 329, 1016, 339, 1014, 326, 1006, 326, 1006, 318, 994, 322, 991, 322, 992, 311, 988, 310, 978, 321, 978, 327, 992, 334, 998, 335, 990, 344, 986, 341, 970, 334, 965, 339, 958, 335, 948, 338, 943, 334, 938, 336, 934, 332, 923, 333, 909, 322, 905, 322, 891, 298, 894, 286, 891, 263};
        int[] jap = {1061, 235, 1078, 244, 1087, 238, 1089, 243, 1088, 248, 1082, 249, 1080, 255, 1066, 257, 1063, 261, 1066, 265, 1071, 270, 1072, 275, 1080, 278, 1075, 290, 1074, 293, 1079, 298, 1077, 303, 1086, 309, 1086, 314, 1078, 311, 1078, 318, 1056, 326, 1048, 327, 1037, 338, 1042, 343, 1051, 336, 1054, 339, 1048, 348, 1034, 359, 1034, 356, 1026, 353, 1036, 330, 1042, 322, 1049, 325, 1051, 318, 1051, 309, 1062, 293, 1064, 281, 1061, 276, 1062, 265, 1058, 262, 1059, 256, 1066, 254, 1067, 244, 1062, 234};
        int[] eastau = {1066, 757, 1069, 683, 1024, 682, 1024, 630, 1027, 628, 1027, 622, 1033, 614, 1040, 619, 1038, 608, 1042, 609, 1045, 617, 1055, 608, 1064, 610, 1065, 614, 1054, 617, 1052, 630, 1059, 630, 1069, 642, 1075, 635, 1082, 609, 1086, 612, 1085, 621, 1090, 626, 1089, 635, 1094, 640, 1093, 648, 1103, 652, 1102, 663, 1108, 666, 1116, 678, 1115, 707, 1098, 735, 1097, 746, 1083, 750, 1082, 757, 1076, 756, 1074, 750, 1066, 757};
        int[] westau = {1018, 622, 1021, 630, 1019, 686, 1064, 688, 1063, 758, 1057, 756, 1054, 752, 1054, 737, 1046, 739, 1046, 736, 1055, 724, 1056, 719, 1049, 722, 1042, 733, 1039, 731, 1034, 720, 1002, 721, 996, 728, 983, 730, 962, 743, 950, 738, 956, 730, 955, 719, 954, 708, 951, 702, 948, 691, 954, 685, 951, 674, 970, 656, 978, 660, 994, 649, 994, 643, 1003, 637, 1007, 635, 1009, 626, 1017, 623, 1021, 630};
        int[] papa = {1010, 554, 1009, 544, 1005, 538, 1005, 531, 1013, 527, 1021, 533, 1022, 546, 1031, 546, 1034, 535, 1040, 532, 1058, 542, 1061, 550, 1070, 550, 1069, 559, 1080, 565, 1074, 570, 1089, 590, 1085, 592, 1075, 590, 1069, 578, 1058, 578, 1058, 585, 1042, 585, 1037, 579, 1029, 578, 1034, 568, 1034, 560, 1030, 557, 1026, 556, 1020, 552, 1013, 555, 1007, 554, 1007, 544, 1005, 537};
        int[] indo = {964, 542, 971, 546, 977, 554, 971, 555, 969, 562, 973, 578, 966, 582, 967, 588, 972, 589, 978, 581, 998, 574, 999, 576, 996, 582, 992, 585, 981, 586, 984, 590, 991, 590, 990, 593, 984, 593, 982, 596, 982, 603, 988, 604, 986, 608, 990, 614, 988, 619, 984, 610, 979, 608, 978, 612, 975, 619, 972, 618, 976, 595, 971, 591, 967, 591, 966, 599, 962, 606, 950, 606, 942, 602, 936, 593, 926, 594, 924, 597, 926, 601, 931, 601, 934, 606, 932, 611, 939, 612, 941, 618, 946, 627, 962, 626, 978, 630, 982, 635, 941, 632, 943, 630, 935, 624, 932, 626, 924, 619, 914, 600, 909, 599, 905, 589, 892, 571, 885, 566, 888, 559, 897, 560, 917, 582, 926, 589, 938, 589, 938, 575, 950, 567, 960, 546, 965, 542};

        Alaska.setAdjacent(new HashSet<Province>(Arrays.asList(Northwest_Territory, Alberta, Kamchatka)));
        Northwest_Territory.setAdjacent(new HashSet<Province>(Arrays.asList(Alaska, Alberta, Ontario, Greenland)));
        Greenland.setAdjacent(new HashSet<Province>(Arrays.asList(Northwest_Territory, Ontario, Eastern_Canada, Iceland)));
        Alberta.setAdjacent(new HashSet<Province>(Arrays.asList(Alaska, Northwest_Territory, Ontario, Western_United_States)));
        Ontario.setAdjacent(new HashSet<Province>(Arrays.asList(Northwest_Territory, Alberta, Eastern_Canada, Eastern_United_States, Western_United_States, Greenland)));
        Eastern_Canada.setAdjacent(new HashSet<Province>(Arrays.asList(Greenland, Ontario, Eastern_United_States)));
        Western_United_States.setAdjacent(new HashSet<Province>(Arrays.asList(Alberta, Eastern_United_States, Ontario, Central_America)));
        Eastern_United_States.setAdjacent(new HashSet<Province>(Arrays.asList(Western_United_States, Ontario, Eastern_Canada, Central_America)));
        Central_America.setAdjacent(new HashSet<Province>(Arrays.asList(Western_United_States, Eastern_United_States, Venezuela)));
        Venezuela.setAdjacent(new HashSet<Province>(Arrays.asList(Brazil, Peru, Central_America)));
        Peru.setAdjacent(new HashSet<Province>(Arrays.asList(Brazil, Venezuela, Argentina)));
        Argentina.setAdjacent(new HashSet<Province>(Arrays.asList(Brazil, Peru)));
        Brazil.setAdjacent(new HashSet<Province>(Arrays.asList(Peru, Venezuela, Argentina, West_Africa)));
        West_Africa.setAdjacent(new HashSet<Province>(Arrays.asList(Western_Europe, Central_Africa, East_Africa, Egypt, Southern_Europe, Brazil)));
        Central_Africa.setAdjacent(new HashSet<Province>(Arrays.asList(West_Africa, East_Africa, South_Africa)));
        South_Africa.setAdjacent(new HashSet<Province>(Arrays.asList(Central_Africa, East_Africa, Madagascar)));
        Madagascar.setAdjacent(new HashSet<Province>(Arrays.asList(South_Africa, East_Africa)));
        East_Africa.setAdjacent(new HashSet<Province>(Arrays.asList(West_Africa, Central_Africa, South_Africa, Madagascar, Egypt, Middle_East)));
        Egypt.setAdjacent(new HashSet<Province>(Arrays.asList(West_Africa, East_Africa, Middle_East, Southern_Europe)));
        Western_Europe.setAdjacent(new HashSet<Province>(Arrays.asList(Southern_Europe, Northern_Europe, United_Kingdom, West_Africa)));
        United_Kingdom.setAdjacent(new HashSet<Province>(Arrays.asList(Iceland, Western_Europe, Northern_Europe, Scandinavia)));
        Iceland.setAdjacent(new HashSet<Province>(Arrays.asList(United_Kingdom, Scandinavia, Greenland)));
        Northern_Europe.setAdjacent(new HashSet<Province>(Arrays.asList(Western_Europe, Southern_Europe, Scandinavia, Russia, United_Kingdom)));
        Southern_Europe.setAdjacent(new HashSet<Province>(Arrays.asList(Western_Europe, Northern_Europe, Russia, West_Africa, Egypt, Middle_East)));
        Scandinavia.setAdjacent(new HashSet<Province>(Arrays.asList(Iceland, United_Kingdom, Northern_Europe, Russia)));
        Russia.setAdjacent(new HashSet<Province>(Arrays.asList(Scandinavia, Northern_Europe, Southern_Europe, Middle_East, Afghanistan, Ural)));
        Middle_East.setAdjacent(new HashSet<Province>(Arrays.asList(Egypt, East_Africa, Southern_Europe, Russia, Afghanistan, India)));
        Afghanistan.setAdjacent(new HashSet<Province>(Arrays.asList(Russia, Middle_East, Ural, China, India)));
        India.setAdjacent(new HashSet<Province>(Arrays.asList(China, Afghanistan, Middle_East, Southeast_Asia)));
        China.setAdjacent(new HashSet<Province>(Arrays.asList(Afghanistan, India, Southeast_Asia, Ural, Siberia, Mongolia)));
        Ural.setAdjacent(new HashSet<Province>(Arrays.asList(Russia, Afghanistan, China, Siberia)));
        Siberia.setAdjacent(new HashSet<Province>(Arrays.asList(Mongolia, China, Ural, Irkutsk, Yakutsk)));
        Mongolia.setAdjacent(new HashSet<Province>(Arrays.asList(China, Japan, Siberia, Irkutsk, Kamchatka)));
        Japan.setAdjacent(new HashSet<Province>(Arrays.asList(Mongolia, Kamchatka)));
        Irkutsk.setAdjacent(new HashSet<Province>(Arrays.asList(Mongolia, Kamchatka, Yakutsk, Siberia)));
        Yakutsk.setAdjacent(new HashSet<Province>(Arrays.asList(Siberia, Kamchatka, Irkutsk)));
        Kamchatka.setAdjacent(new HashSet<Province>(Arrays.asList(Alaska, Irkutsk, Mongolia, Japan, Yakutsk)));
        Indonesia.setAdjacent(new HashSet<Province>(Arrays.asList(Southeast_Asia, Western_Australia, New_Guinea)));
        New_Guinea.setAdjacent(new HashSet<Province>(Arrays.asList(Indonesia, Eastern_Australia, Western_Australia)));
        Eastern_Australia.setAdjacent(new HashSet<Province>(Arrays.asList(Western_Australia, New_Guinea)));
        Western_Australia.setAdjacent(new HashSet<Province>(Arrays.asList(New_Guinea, Indonesia, Eastern_Australia)));

        GraphicProvince[] graphprovs = {new GraphicProvince(Alaska, alaska), new GraphicProvince(Northwest_Territory, northwestCan), new GraphicProvince(Alberta, alberta), new GraphicProvince(Ontario, ontario), new GraphicProvince(Eastern_Canada, easternCan), new GraphicProvince(Greenland, greenland), 
            new GraphicProvince(Central_America, centralamer), new GraphicProvince(Western_United_States, westernUS), new GraphicProvince(Eastern_United_States, easternUS), new GraphicProvince(Venezuela, venezuela), new GraphicProvince(Peru, peru), new GraphicProvince(Brazil, brazil), new GraphicProvince(Argentina, argentina), 
            new GraphicProvince(Iceland, iceland), new GraphicProvince(United_Kingdom, uk), new GraphicProvince(Scandinavia, scandinavia), new GraphicProvince(Russia, russia), new GraphicProvince(Northern_Europe, northeu), new GraphicProvince(Western_Europe, westeu), new GraphicProvince(Southern_Europe, southeu), 
            new GraphicProvince(West_Africa, westafr), new GraphicProvince(Egypt, egypt), new GraphicProvince(East_Africa, eastafr), new GraphicProvince(Central_Africa, centafr), new GraphicProvince(South_Africa, southafr), new GraphicProvince(Madagascar, mad), new GraphicProvince(Middle_East, mideast),
            new GraphicProvince(Ural, ural), new GraphicProvince(Afghanistan, afga), new GraphicProvince(India, india), new GraphicProvince(China, china), new GraphicProvince(Southeast_Asia, southasia), new GraphicProvince(Siberia, siberia), new GraphicProvince(Yakutsk, yak),
            new GraphicProvince(Kamchatka, kam), new GraphicProvince(Irkutsk, irk), new GraphicProvince(Mongolia, mong), new GraphicProvince(Japan, jap), new GraphicProvince(Eastern_Australia, eastau), new GraphicProvince(Western_Australia, westau), new GraphicProvince(New_Guinea, papa), new GraphicProvince(Indonesia, indo)};

        Continent northAmerica = new Continent("North America", 5, new HashSet<Province>(Arrays.asList(Alaska, Northwest_Territory, Alberta, Ontario, Eastern_Canada, Greenland, Central_America, Western_United_States, Eastern_United_States)));
        Continent southAmerica = new Continent("South America", 2, new HashSet<Province>(Arrays.asList(Venezuela, Peru, Brazil, Argentina)));
        Continent europe = new Continent("Europe", 5, new HashSet<Province>(Arrays.asList(Iceland, United_Kingdom, Scandinavia, Russia, Northern_Europe, Western_Europe, Southern_Europe)));
        Continent africa = new Continent("Africa", 3, new HashSet<Province>(Arrays.asList(West_Africa, Egypt, East_Africa, Central_Africa, South_Africa, Madagascar, Middle_East)));
        Continent asia = new Continent("Asia", 7, new HashSet<Province>(Arrays.asList(Ural, Afghanistan, India, China, Southeast_Asia, Siberia, Yakutsk, Kamchatka, Irkutsk, Mongolia, Japan)));
        Continent oceania = new Continent("Oceania", 2, new HashSet<Province>(Arrays.asList(Eastern_Australia, Western_Australia, New_Guinea, Indonesia)));
        World world = new World("Worldy World");
        world.setContinents(new HashSet<Continent>(Arrays.asList(northAmerica, southAmerica, europe, africa, asia, oceania)));
        world.setProvinces(new HashSet<Province>(Arrays.asList(Alaska, Northwest_Territory, Alberta, Ontario, Eastern_Canada, Greenland, Central_America, Western_United_States, Eastern_United_States, Venezuela, Peru, Brazil, Argentina, Iceland, United_Kingdom, Scandinavia, Russia, Northern_Europe, Western_Europe, Southern_Europe, West_Africa, Egypt, East_Africa, Central_Africa, South_Africa, Madagascar, Middle_East, Ural, Afghanistan, India, China, Southeast_Asia, Siberia, Yakutsk, Kamchatka, Irkutsk, Mongolia, Japan, Eastern_Australia, Western_Australia, New_Guinea, Indonesia)));
        
        Game game = new Game(players, world, graphprovs);

        RenderEarth test = new RenderEarth(graphprovs, players, game);

        game.startGame(240, test);
    }

}
