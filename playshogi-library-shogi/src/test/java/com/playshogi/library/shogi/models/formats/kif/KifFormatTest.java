package com.playshogi.library.shogi.models.formats.kif;

import com.playshogi.library.models.record.GameRecord;
import com.playshogi.library.shogi.models.formats.usf.UsfFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KifFormatTest {

    private final String KIF_81 = "#KIF version=2.0 encoding=UTF-8\n" +
            "開始日時：2020/08/10\n" +
            "場所：81Dojo\n" +
            "持ち時間：5分+10秒\n" +
            "手合割：平手\n" +
            "先手：AAA\n" +
            "後手：BBB\n" +
            "手数----指手---------消費時間--\n" +
            "1   ７六歩(77)   (0:4/0:0:4)\n" +
            "2   ３四歩(33)   (0:1/0:0:1)\n" +
            "3   ２六歩(27)   (0:1/0:0:5)\n" +
            "4   ８四歩(83)   (0:2/0:0:3)\n" +
            "5   ２五歩(26)   (0:1/0:0:6)\n" +
            "6   ８五歩(84)   (0:1/0:0:4)\n" +
            "7   ７八金(69)   (0:1/0:0:7)\n" +
            "8   ８八角成(22)   (0:2/0:0:6)\n" +
            "9   同　銀(79)   (0:1/0:0:8)\n" +
            "10   ２二銀(31)   (0:1/0:0:7)\n" +
            "11   ７七銀(88)   (0:1/0:0:9)\n" +
            "12   ３三銀(22)   (0:1/0:0:8)\n" +
            "13   ３八銀(39)   (0:1/0:0:10)\n" +
            "14   ３二金(41)   (0:1/0:0:9)\n" +
            "15   ２七銀(38)   (0:2/0:0:12)\n" +
            "16   ７二銀(71)   (0:14/0:0:23)\n" +
            "17   ２六銀(27)   (0:2/0:0:14)\n" +
            "18   １四歩(13)   (0:2/0:0:25)\n" +
            "19   １六歩(17)   (0:3/0:0:17)\n" +
            "20   ９四歩(93)   (0:19/0:0:44)\n" +
            "21   ６八玉(59)   (0:9/0:0:26)\n" +
            "22   ７四歩(73)   (0:2/0:0:46)\n" +
            "23   １五歩(16)   (0:3/0:0:29)\n" +
            "24   同　歩(14)   (0:1/0:0:47)\n" +
            "25   同　銀(26)   (0:1/0:0:30)\n" +
            "26   同　香(11)   (0:2/0:0:49)\n" +
            "27   同　香(19)   (0:1/0:0:31)\n" +
            "28   １九角打   (0:15/0:1:4)\n" +
            "29   １八飛(28)   (0:12/0:0:43)\n" +
            "30   ２七銀打   (0:2/0:1:6)\n" +
            "31   １九飛(18)   (0:24/0:1:7)\n" +
            "32   １八歩打   (0:2/0:1:8)\n" +
            "33   １二香成(15)   (0:41/0:1:48)\n" +
            "34   １九歩成(18)   (0:1/0:1:9)\n" +
            "35   ２一成香(12)   (0:2/0:1:50)\n" +
            "36   ２九と(19)   (0:4/0:1:13)\n" +
            "37   ５六香打   (0:14/0:2:4)\n" +
            "38   ４四銀(33)   (0:4/0:1:17)\n" +
            "39   ６五桂打   (0:29/0:2:33)\n" +
            "40   ４二金(32)   (0:4/0:1:21)\n" +
            "41   ３一角打   (0:14/0:2:47)\n" +
            "42   ４一桂打   (0:49/0:2:10)\n" +
            "43   ４二角成(31)   (1:28/0:4:15)\n" +
            "44   同　玉(51)   (0:3/0:2:13)\n" +
            "45   ３一角打   (0:2/0:4:17)\n" +
            "46   ５二玉(42)   (0:1/0:2:14)\n" +
            "47   ４二金打   (0:6/0:4:23)\n" +
            "48   ６二玉(52)   (0:1/0:2:15)\n" +
            "49   ４一金(42)   (0:1/0:4:24)\n" +
            "50   ２八飛打   (1:17/0:3:32)\n" +
            "51   ７九玉(68)   (0:8/0:4:32)\n" +
            "52   ７一玉(62)   (0:1/0:3:33)\n" +
            "53   ５三香成(56)   (0:34/0:5:6)\n" +
            "54   ８六歩(85)   (0:1/0:3:34)\n" +
            "55   同　銀(77)   (0:7/0:5:13)\n" +
            "56   同　飛(82)   (0:14/0:3:48)\n" +
            "57   ６二成香(53)   (0:7/0:5:20)\n" +
            "58   同　金(61)   (0:5/0:3:53)\n" +
            "59   ８六角成(31)   (0:2/0:5:22)\n" +
            "60   ８三香打   (0:3/0:3:56)\n" +
            "61   ５一飛打   (0:6/0:5:28)\n" +
            "62   ６一金(62)   (0:26/0:4:22)\n" +
            "63   同　飛成(51)   (0:8/0:5:36)\n" +
            "64   同　玉(71)   (0:11/0:4:33)\n" +
            "65   ５一金(41)   (0:8/0:5:44)\n" +
            "66   ７一玉(61)   (0:10/0:4:43)\n" +
            "67   ５二金(51)   (0:8/0:5:52)\n" +
            "68   ８六香(83)   (0:5/0:4:48)\n" +
            "69   ６二金打   (0:4/0:5:56)\n" +
            "70   ８二玉(71)   (0:3/0:4:51)\n" +
            "71   ７二金(62)   (0:5/0:6:1)\n" +
            "72   同　玉(82)   (0:1/0:4:52)\n" +
            "73   ８六歩(87)   (0:9/0:6:10)\n" +
            "74   ８七角打   (0:14/0:5:6)\n" +
            "75   ５八香打   (0:9/0:6:19)\n" +
            "76   ７六角成(87)   (0:8/0:5:14)\n" +
            "77   ７七桂(89)   (0:8/0:6:27)\n" +
            "78   ８七歩打   (0:3/0:5:17)\n" +
            "79   ８五銀打   (0:8/0:6:35)\n" +
            "80   ８八金打   (0:4/0:5:21)\n" +
            "81   ６九玉(79)   (0:10/0:6:45)\n" +
            "82   ７八金(88)   (0:1/0:5:22)\n" +
            "83   投了   (0:3/0:6:48)";

    private final String KIF_24 = "開始日時：Aug 31, 2020 6:39:53 PM\n" +
            "棋戦：Rating Game Room(short-timelimited)\n" +
            "手合割：平手\n" +
            "先手：AAA(1779)\n" +
            "後手：BBB(1795)\n" +
            "手数----指手---------消費時間--\n" +
            "1 ７六歩(77)   ( 0:02/00:00:02)\n" +
            "2 ３四歩(33)   ( 0:00/00:00:00)\n" +
            "3 ２六歩(27)   ( 0:02/00:00:04)\n" +
            "4 ５四歩(53)   ( 0:02/00:00:02)\n" +
            "5 ２五歩(26)   ( 0:03/00:00:07)\n" +
            "6 ５二飛(82)   ( 0:01/00:00:03)\n" +
            "7 ４八銀(39)   ( 0:01/00:00:08)\n" +
            "8 ６二玉(51)   ( 0:01/00:00:04)\n" +
            "9 ２二角成(88)   ( 0:07/00:00:15)\n" +
            "10 ２二銀(31)   ( 0:02/00:00:06)\n" +
            "11 ９六歩(97)   ( 0:01/00:00:16)\n" +
            "12 ９四歩(93)   ( 0:01/00:00:07)\n" +
            "13 ７八銀(79)   ( 0:01/00:00:17)\n" +
            "14 ７二玉(62)   ( 0:01/00:00:08)\n" +
            "15 ６八玉(59)   ( 0:02/00:00:19)\n" +
            "16 ８二玉(72)   ( 0:01/00:00:09)\n" +
            "17 ４六歩(47)   ( 0:01/00:00:20)\n" +
            "18 ７二銀(71)   ( 0:01/00:00:10)\n" +
            "19 ６六歩(67)   ( 0:15/00:00:35)\n" +
            "20 ６四歩(63)   ( 0:01/00:00:11)\n" +
            "21 ２四歩(25)   ( 0:06/00:00:41)\n" +
            "22 ２四歩(23)   ( 0:01/00:00:12)\n" +
            "23 ２四飛(28)   ( 0:01/00:00:42)\n" +
            "24 ２三歩打     ( 0:01/00:00:13)\n" +
            "25 ２八飛(24)   ( 0:07/00:00:49)\n" +
            "26 ５五歩(54)   ( 0:01/00:00:14)\n" +
            "27 ４七銀(48)   ( 0:02/00:00:51)\n" +
            "28 ５一飛(52)   ( 0:02/00:00:16)\n" +
            "29 １六角打     ( 0:15/00:01:06)\n" +
            "30 ３二金(41)   ( 0:06/00:00:22)\n" +
            "31 ３四角(16)   ( 0:02/00:01:08)\n" +
            "32 ５四飛(51)   ( 0:16/00:00:38)\n" +
            "33 ４五角(34)   ( 0:02/00:01:10)\n" +
            "34 ２四飛(54)   ( 0:16/00:00:54)\n" +
            "35 ２五歩打     ( 0:18/00:01:28)\n" +
            "36 ４四飛(24)   ( 0:03/00:00:57)\n" +
            "37 ７九玉(68)   ( 0:05/00:01:33)\n" +
            "38 ３三銀(22)   ( 0:04/00:01:01)\n" +
            "39 ８八玉(79)   ( 0:06/00:01:39)\n" +
            "40 ３四銀(33)   ( 0:02/00:01:03)\n" +
            "41 ６七角(45)   ( 0:10/00:01:49)\n" +
            "42 ６五歩(64)   ( 0:01/00:01:04)\n" +
            "43 ７七銀(78)   ( 0:25/00:02:14)\n" +
            "44 ３三桂(21)   ( 0:09/00:01:13)\n" +
            "45 ２四歩(25)   ( 0:05/00:02:19)\n" +
            "46 ２四歩(23)   ( 0:02/00:01:15)\n" +
            "47 ２四飛(28)   ( 0:02/00:02:21)\n" +
            "48 ２三金(32)   ( 0:01/00:01:16)\n" +
            "49 ２八飛(24)   ( 0:10/00:02:31)\n" +
            "50 ２四歩打     ( 0:22/00:01:38)\n" +
            "51 ６五歩(66)   ( 0:09/00:02:40)\n" +
            "52 ２二金(23)   ( 0:15/00:01:53)\n" +
            "53 反則勝ち";

    @Test
    public void readFromShogiClub24Kifu() {
        GameRecord kif24 = KifFormat.INSTANCE.read(KIF_24);
        assertEquals("USF:1.0\n" +
                "^*lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b " +
                "-:7g7f3c3d2g2f5c5d2f2e8b5b3i4h5a6b8h2B3a2b9g9f9c9d7i7h6b7b5i6h7b8b4g4f7a7b6g6f6c6d2e2d2c2d2h2dp" +
                "*2c2d2h5d5e4h4g5b5aB*1f4a3b1f3d5a5d3d4e5d2dP" +
                "*2e2d4d6h7i2b3c7i8h3c3d4e6g6d6e7h7g2a3c2e2d2c2d2h2d3b2c2d2hp*2d6f6e2c2bFOUL\n" +
                "BN:AAA(1779)\n" +
                "WN:BBB(1795)\n" +
                "GD:Aug 31, 2020 6:39:53 PM\n" +
                "GQ:UNKNOWN", UsfFormat.INSTANCE.write(kif24));
    }

    @Test
    public void readFrom81DojoKifu() {
        GameRecord kif81 = KifFormat.INSTANCE.read(KIF_81);
        assertEquals("USF:1.0\n" +
                "^*lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b " +
                "-:7g7f3c3d2g2f8c8d2f2e8d8e6i7h2b8H7i8h3a2b8h7g2b3c3i3h4a3b3h2g7a7b2g2f1c1d1g1f9c9d5i6h7c7d1f1e1d1" +
                "e2f1e1a1e1i1eb*1i2h1hs*2g1h1ip*1h1e1B1h1I1b2a1i2iL*5f3c4dN*6e3b4bB*3an*4a3a4B5a4bB*3a4b5bG*4b5b6b4" +
                "b4ar*2h6h7i6b7a5f5C8e8f7g8f8b8f5c6b6a6b3a8Fl*8cR*5a6b6a5a6A7a6a4a5a6a7a5a5b8c8fG*6b7a8b6b7b8b7b8g8f" +
                "b*8gL*5h8g7F8i7gp*8gS*8eg*8h7i6i8h7hRSGN\n" +
                "BN:AAA\n" +
                "WN:BBB\n" +
                "GD:2020/08/10\n" +
                "GQ:81Dojo", UsfFormat.INSTANCE.write(kif81));
    }
}