package com.aiba.haimaelc.tools;

import android.text.TextUtils;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.model.SortCity;
import com.amap.api.location.AMapLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    public static List<SortCity> sortCityList = new ArrayList<>();

    public static List<SortCity> getSortCityList() {
        if (sortCityList.size() > 0) {
            return sortCityList;
        }
        sortCityList.add(new SortCity("484", "AKS", "31", "阿克苏"));
        sortCityList.add(new SortCity("502", "AL", "32", "阿里"));
        sortCityList.add(new SortCity("349", "ALSM", "22", "阿拉善盟"));
        sortCityList.add(new SortCity("485", "ALT", "31", "阿勒泰"));
        sortCityList.add(new SortCity("347", "AM", "21", "澳门"));
        sortCityList.add(new SortCity("80", "AQ", "4", "安庆"));
        sortCityList.add(new SortCity("334", "AS", "20", "鞍山"));
        sortCityList.add(new SortCity("170", "AS", "9", "安顺"));
        sortCityList.add(new SortCity("486", "ATS", "31", "阿图什"));
        sortCityList.add(new SortCity("231", "AY", "13", "安阳"));
        sortCityList.add(new SortCity("81", "BB", "4", "蚌埠"));
        sortCityList.add(new SortCity("472", "BC", "30", "北辰"));
        sortCityList.add(new SortCity("323", "BC", "19", "白城"));
        sortCityList.add(new SortCity("481", "BD", "30", "宝坻"));
        sortCityList.add(new SortCity("202", "BD", "11", "保定"));
        sortCityList.add(new SortCity("203", "BDH", "11", "北戴河"));
        sortCityList.add(new SortCity("158", "BH", "8", "北海"));
        sortCityList.add(new SortCity("", "BJ", "1", "北京"));
        sortCityList.add(new SortCity("409", "BJ", "27", "宝鸡"));
        sortCityList.add(new SortCity("217", "BJ", "12", "北安"));
        sortCityList.add(new SortCity("171", "BJ", "9", "毕节"));
        sortCityList.add(new SortCity("487", "BL", "31", "博乐"));
        sortCityList.add(new SortCity("51", "BN", "3", "巴南"));
        sortCityList.add(new SortCity("47", "BP", "3", "北碚"));
        sortCityList.add(new SortCity("157", "BS", "8", "百色"));
        sortCityList.add(new SortCity("30", "BS", "2", "宝山"));
        sortCityList.add(new SortCity("59", "BS", "3", "壁山"));
        sortCityList.add(new SortCity("324", "BS", "19", "白山"));
        sortCityList.add(new SortCity("510", "BS", "33", "保山"));
        sortCityList.add(new SortCity("192", "BSLZ", "10", "白沙黎族"));
        sortCityList.add(new SortCity("350", "BT", "22", "包头"));
        sortCityList.add(new SortCity("196", "BTLZ", "10", "保亭黎族"));
        sortCityList.add(new SortCity("335", "BX", "20", "本溪"));
        sortCityList.add(new SortCity("115", "BY", "6", "白银"));
        sortCityList.add(new SortCity("419", "BZ", "28", "巴中"));
        sortCityList.add(new SortCity("375", "BZ", "25", "滨州"));
        sortCityList.add(new SortCity("322", "CC", "19", "长春"));
        sortCityList.add(new SortCity("205", "CD", "11", "承德"));
        sortCityList.add(new SortCity("503", "CD", "32", "昌都"));
        sortCityList.add(new SortCity("418", "CD", "28", "成都"));
        sortCityList.add(new SortCity("270", "CD", "16", "常德"));
        sortCityList.add(new SortCity("351", "CF", "22", "赤峰"));
        sortCityList.add(new SortCity("132", "CH", "7", "澄海"));
        sortCityList.add(new SortCity("83", "CH", "4", "巢湖"));
        sortCityList.add(new SortCity("488", "CJ", "31", "昌吉"));
        sortCityList.add(new SortCity("193", "CJLZ", "10", "昌江黎族"));
        sortCityList.add(new SortCity("61", "CK", "3", "城口"));
        sortCityList.add(new SortCity("19", "CM", "2", "崇明"));
        sortCityList.add(new SortCity("189", "CM", "10", "澄迈"));
        sortCityList.add(new SortCity("23", "CN", "2", "长宁"));
        sortCityList.add(new SortCity("13", "CP", "1", "昌平"));
        sortCityList.add(new SortCity("", "CQ", "3", "重庆"));
        sortCityList.add(new SortCity("269", "CS", "16", "长沙"));
        sortCityList.add(new SortCity("284", "CS", "17", "常熟"));
        sortCityList.add(new SortCity("53", "CS", "3", "长寿"));
        sortCityList.add(new SortCity("3", "CW", "1", "崇文"));
        sortCityList.add(new SortCity("511", "CX", "33", "楚雄"));
        sortCityList.add(new SortCity("530", "CX", "34", "慈溪"));
        sortCityList.add(new SortCity("5", "CY", "1", "朝阳"));
        sortCityList.add(new SortCity("336", "CY", "20", "朝阳"));
        sortCityList.add(new SortCity("130", "CY", "7", "潮阳"));
        sortCityList.add(new SortCity("395", "CZ", "26", "长治"));
        sortCityList.add(new SortCity("285", "CZ", "17", "常州"));
        sortCityList.add(new SortCity("131", "CZ", "7", "潮州"));
        sortCityList.add(new SortCity("84", "CZ", "4", "滁州"));
        sortCityList.add(new SortCity("549", "CZ", "34", "嵊州"));
        sortCityList.add(new SortCity("204", "CZ", "11", "沧州"));
        sortCityList.add(new SortCity("271", "CZ", "16", "郴州"));
        sortCityList.add(new SortCity("187", "DA", "10", "定安"));
        sortCityList.add(new SortCity("1", "DC", "1", "东城"));
        sortCityList.add(new SortCity("513", "DC", "33", "东川"));
        sortCityList.add(new SortCity("338", "DD", "20", "丹东"));
        sortCityList.add(new SortCity("42", "DDK", "3", "大渡口"));
        sortCityList.add(new SortCity("186", "DF", "10", "东方"));
        sortCityList.add(new SortCity("478", "DG", "30", "大港"));
        sortCityList.add(new SortCity("133", "DG", "7", "东莞"));
        sortCityList.add(new SortCity("117", "DH", "6", "敦煌"));
        sortCityList.add(new SortCity("531", "DH", "34", "定海"));
        sortCityList.add(new SortCity("172", "DJ", "9", "都匀"));
        sortCityList.add(new SortCity("63", "DJ", "3", "垫江"));
        sortCityList.add(new SortCity("422", "DJY", "28", "都江堰"));
        sortCityList.add(new SortCity("337", "DL", "20", "大连"));
        sortCityList.add(new SortCity("466", "DL", "30", "东丽"));
        sortCityList.add(new SortCity("509", "DL", "33", "大理"));
        sortCityList.add(new SortCity("512", "DL", "33", "大理"));
        sortCityList.add(new SortCity("366", "DLH", "24", "德令哈"));
        sortCityList.add(new SortCity("218", "DQ", "12", "大庆"));
        sortCityList.add(new SortCity("489", "DS", "31", "东山"));
        sortCityList.add(new SortCity("352", "DS", "22", "东胜"));
        sortCityList.add(new SortCity("396", "DT", "26", "大同"));
        sortCityList.add(new SortCity("14", "DX", "1", "大兴"));
        sortCityList.add(new SortCity("116", "DX", "6", "定西"));
        sortCityList.add(new SortCity("219", "DXAL", "12", "大兴安岭"));
        sortCityList.add(new SortCity("378", "DY", "25", "东营"));
        sortCityList.add(new SortCity("421", "DY", "28", "德阳"));
        sortCityList.add(new SortCity("420", "DZ", "28", "达州"));
        sortCityList.add(new SortCity("377", "DZ", "25", "德州"));
        sortCityList.add(new SortCity("57", "DZ", "3", "大足"));
        sortCityList.add(new SortCity("423", "EMS", "28", "峨眉山"));
        sortCityList.add(new SortCity("253", "ES", "15", "恩施"));
        sortCityList.add(new SortCity("254", "EZ", "15", "鄂州"));
        sortCityList.add(new SortCity("100", "FA", "5", "福安"));
        sortCityList.add(new SortCity("160", "FCG", "8", "防城港"));
        sortCityList.add(new SortCity("62", "FD", "3", "丰都"));
        sortCityList.add(new SortCity("532", "FH", "34", "奉化"));
        sortCityList.add(new SortCity("68", "FJ", "3", "奉节"));
        sortCityList.add(new SortCity("40", "FL", "3", "涪陵"));
        sortCityList.add(new SortCity("424", "FL", "28", "涪陵"));
        sortCityList.add(new SortCity("257", "FM", "15", "荆门"));
        sortCityList.add(new SortCity("206", "FR", "11", "丰润"));
        sortCityList.add(new SortCity("339", "FS", "20", "抚顺"));
        sortCityList.add(new SortCity("134", "FS", "7", "佛山"));
        sortCityList.add(new SortCity("10", "FS", "1", "房山"));
        sortCityList.add(new SortCity("6", "FT", "1", "丰台"));
        sortCityList.add(new SortCity("37", "FX", "2", "奉贤"));
        sortCityList.add(new SortCity("340", "FX", "20", "阜新"));
        sortCityList.add(new SortCity("85", "FY", "4", "阜阳"));
        sortCityList.add(new SortCity("432", "FY", "28", "绵阳"));
        sortCityList.add(new SortCity("127", "FYG", "6", "嘉峪关"));
        sortCityList.add(new SortCity("310", "FZ", "18", "抚州"));
        sortCityList.add(new SortCity("99", "FZ", "5", "福州"));
        sortCityList.add(new SortCity("427", "FZG", "28", "九寨沟"));
        sortCityList.add(new SortCity("436", "FZH", "28", "攀枝花"));
        sortCityList.add(new SortCity("425", "GA", "28", "广安"));
        sortCityList.add(new SortCity("367", "GEM", "24", "格尔木"));
        sortCityList.add(new SortCity("167", "GG", "8", "贵港"));
        sortCityList.add(new SortCity("368", "GH", "24", "共和"));
        sortCityList.add(new SortCity("514", "GJ", "33", "个旧"));
        sortCityList.add(new SortCity("159", "GL", "8", "桂林"));
        sortCityList.add(new SortCity("118", "GN", "6", "甘南"));
        sortCityList.add(new SortCity("448", "GX", "29", "高雄"));
        sortCityList.add(new SortCity("361", "GY", "23", "固原"));
        sortCityList.add(new SortCity("426", "GY", "28", "广元"));
        sortCityList.add(new SortCity("129", "GZ", "7", "广州"));
        sortCityList.add(new SortCity("311", "GZ", "18", "赣州"));
        sortCityList.add(new SortCity("287", "HA", "17", "淮安"));
        sortCityList.add(new SortCity("473", "HB", "30", "河北"));
        sortCityList.add(new SortCity("87", "HB", "4", "淮北"));
        sortCityList.add(new SortCity("232", "HB", "13", "鹤壁"));
        sortCityList.add(new SortCity("76", "HC", "3", "合川"));
        sortCityList.add(new SortCity("325", "HC", "19", "珲春"));
        sortCityList.add(new SortCity("161", "HC", "8", "河池"));
        sortCityList.add(new SortCity("8", "HD", "1", "海淀"));
        sortCityList.add(new SortCity("467", "HD", "30", "河东"));
        sortCityList.add(new SortCity("369", "HD", "24", "海东"));
        sortCityList.add(new SortCity("207", "HD", "11", "邯郸"));
        sortCityList.add(new SortCity("216", "HEB", "12", "哈尔滨"));
        sortCityList.add(new SortCity("79", "HF", "4", "合肥"));
        sortCityList.add(new SortCity("477", "HG", "30", "汉沽"));
        sortCityList.add(new SortCity("220", "HG", "12", "鹤岗"));
        sortCityList.add(new SortCity("255", "HG", "15", "黄冈"));
        sortCityList.add(new SortCity("221", "HH", "12", "黑河"));
        sortCityList.add(new SortCity("88", "HH", "4", "淮化"));
        sortCityList.add(new SortCity("273", "HH", "16", "怀化"));
        sortCityList.add(new SortCity("348", "HHHT", "22", "呼和浩特"));
        sortCityList.add(new SortCity("179", "HK", "10", "海口"));
        sortCityList.add(new SortCity("27", "HK", "2", "虹口"));
        sortCityList.add(new SortCity("456", "HL", "29", "花莲"));
        sortCityList.add(new SortCity("341", "HLD", "20", "葫芦岛"));
        sortCityList.add(new SortCity("353", "HLE", "22", "海拉尔"));
        sortCityList.add(new SortCity("490", "HM", "31", "哈密"));
        sortCityList.add(new SortCity("397", "HM", "26", "候马"));
        sortCityList.add(new SortCity("286", "HM", "17", "海门"));
        sortCityList.add(new SortCity("89", "HN", "4", "淮南"));
        sortCityList.add(new SortCity("465", "HP", "30", "和平"));
        sortCityList.add(new SortCity("20", "HP", "2", "黄浦"));
        sortCityList.add(new SortCity("475", "HQ", "30", "红挢"));
        sortCityList.add(new SortCity("16", "HR", "1", "怀柔"));
        sortCityList.add(new SortCity("90", "HS", "4", "黄山"));
        sortCityList.add(new SortCity("256", "HS", "15", "黄石"));
        sortCityList.add(new SortCity("208", "HS", "11", "衡水"));
        sortCityList.add(new SortCity("491", "HT", "31", "和田"));
        sortCityList.add(new SortCity("469", "HX", "30", "河西"));
        sortCityList.add(new SortCity("534", "HY", "34", "黄岩"));
        sortCityList.add(new SortCity("272", "HY", "16", "衡阳"));
        sortCityList.add(new SortCity("135", "HY", "7", "河源"));
        sortCityList.add(new SortCity("533", "HY", "34", "海盐"));
        sortCityList.add(new SortCity("528", "HZ", "34", "杭州"));
        sortCityList.add(new SortCity("535", "HZ", "34", "湖州"));
        sortCityList.add(new SortCity("233", "HZ", "13", "潢川"));
        sortCityList.add(new SortCity("162", "HZ", "8", "贺州"));
        sortCityList.add(new SortCity("82", "HZ", "4", "亳州"));
        sortCityList.add(new SortCity("410", "HZ", "27", "汉中"));
        sortCityList.add(new SortCity("379", "HZ", "25", "菏泽"));
        sortCityList.add(new SortCity("136", "HZ", "7", "惠州"));
        sortCityList.add(new SortCity("24", "JA", "2", "静安"));
        sortCityList.add(new SortCity("312", "JA", "18", "吉安"));
        sortCityList.add(new SortCity("43", "JB", "3", "江北"));
        sortCityList.add(new SortCity("119", "JC", "6", "金昌"));
        sortCityList.add(new SortCity("398", "JC", "26", "晋城"));
        sortCityList.add(new SortCity("31", "JD", "2", "嘉定"));
        sortCityList.add(new SortCity("288", "JD", "17", "江都"));
        sortCityList.add(new SortCity("313", "JDZ", "18", "景德镇"));
        sortCityList.add(new SortCity("314", "JGS", "18", "井冈山"));
        sortCityList.add(new SortCity("537", "JH", "34", "金华"));
        sortCityList.add(new SortCity("480", "JH", "30", "静海"));
        sortCityList.add(new SortCity("515", "JH", "33", "景洪"));
        sortCityList.add(new SortCity("91", "JHS", "4", "九华山"));
        sortCityList.add(new SortCity("109", "JJ", "5", "晋江"));
        sortCityList.add(new SortCity("315", "JJ", "18", "九江"));
        sortCityList.add(new SortCity("445", "JL", "29", "基隆"));
        sortCityList.add(new SortCity("75", "JL", "3", "江津"));
        sortCityList.add(new SortCity("250", "JL", "14", "九龙"));
        sortCityList.add(new SortCity("328", "JL", "19", "吉林"));
        sortCityList.add(new SortCity("45", "JLP", "3", "九龙坡"));
        sortCityList.add(new SortCity("137", "JM", "7", "江门"));
        sortCityList.add(new SortCity("460", "JM", "29", "金门"));
        sortCityList.add(new SortCity("222", "JMS", "12", "佳木斯"));
        sortCityList.add(new SortCity("374", "JN", "25", "济南"));
        sortCityList.add(new SortCity("354", "JN", "22", "集宁"));
        sortCityList.add(new SortCity("380", "JN", "25", "济宁"));
        sortCityList.add(new SortCity("470", "JN", "30", "津南"));
        sortCityList.add(new SortCity("120", "JQ", "6", "酒泉"));
        sortCityList.add(new SortCity("546", "JS", "34", "江山"));
        sortCityList.add(new SortCity("33", "JS", "2", "金山"));
        sortCityList.add(new SortCity("274", "JS", "16", "吉首"));
        sortCityList.add(new SortCity("482", "JX", "30", "蓟县"));
        sortCityList.add(new SortCity("536", "JX", "34", "嘉兴"));
        sortCityList.add(new SortCity("223", "JX", "12", "鸡西"));
        sortCityList.add(new SortCity("138", "JY", "7", "揭阳"));
        sortCityList.add(new SortCity("289", "JY", "17", "江阴"));
        sortCityList.add(new SortCity("235", "JY", "13", "济源"));
        sortCityList.add(new SortCity("342", "JZ", "20", "锦州"));
        sortCityList.add(new SortCity("234", "JZ", "13", "焦作"));
        sortCityList.add(new SortCity("258", "JZ", "15", "荆州"));
        sortCityList.add(new SortCity("494", "KC", "31", "库车"));
        sortCityList.add(new SortCity("428", "KD", "28", "康定"));
        sortCityList.add(new SortCity("495", "KEL", "31", "库尔勒"));
        sortCityList.add(new SortCity("236", "KF", "13", "开封"));
        sortCityList.add(new SortCity("173", "KL", "9", "凯里"));
        sortCityList.add(new SortCity("493", "KLMY", "31", "克拉玛依"));
        sortCityList.add(new SortCity("508", "KM", "33", "昆明"));
        sortCityList.add(new SortCity("139", "KP", "7", "开平"));
        sortCityList.add(new SortCity("290", "KS", "17", "昆山"));
        sortCityList.add(new SortCity("492", "KS", "31", "喀什"));
        sortCityList.add(new SortCity("496", "KT", "31", "奎屯"));
        sortCityList.add(new SortCity("66", "KX", "3", "开县"));
        sortCityList.add(new SortCity("516", "KY", "33", "开远"));
        sortCityList.add(new SortCity("92", "LA", "4", "六安"));
        sortCityList.add(new SortCity("538", "LA", "34", "临安"));
        sortCityList.add(new SortCity("164", "LB", "8", "来宾"));
        sortCityList.add(new SortCity("393", "LB", "25", "淄博"));
        sortCityList.add(new SortCity("86", "LC", "4", "贵池"));
        sortCityList.add(new SortCity("382", "LC", "25", "聊城"));
        sortCityList.add(new SortCity("517", "LC", "33", "临沧"));
        sortCityList.add(new SortCity("275", "LD", "16", "娄底"));
        sortCityList.add(new SortCity("194", "LDLZ", "10", "乐东黎族"));
        sortCityList.add(new SortCity("383", "LF", "25", "临沂"));
        sortCityList.add(new SortCity("400", "LF", "26", "临汾"));
        sortCityList.add(new SortCity("209", "LF", "11", "廊坊"));
        sortCityList.add(new SortCity("190", "LG", "10", "临高"));
        sortCityList.add(new SortCity("539", "LH", "34", "临海"));
        sortCityList.add(new SortCity("237", "LH", "13", "漯河"));
        sortCityList.add(new SortCity("355", "LH", "22", "临河"));
        sortCityList.add(new SortCity("518", "LJ", "33", "丽江"));
        sortCityList.add(new SortCity("519", "LK", "33", "六库"));
        sortCityList.add(new SortCity("60", "LP", "3", "梁平"));
        sortCityList.add(new SortCity("174", "LPS", "9", "六盘水"));
        sortCityList.add(new SortCity("429", "LS", "28", "乐山"));
        sortCityList.add(new SortCity("540", "LS", "34", "丽水"));
        sortCityList.add(new SortCity("501", "LS", "32", "拉萨"));
        sortCityList.add(new SortCity("316", "LS", "18", "庐山"));
        sortCityList.add(new SortCity("399", "LS", "26", "离石"));
        sortCityList.add(new SortCity("195", "LSLZ", "10", "陵水黎族"));
        sortCityList.add(new SortCity("21", "LW", "2", "卢湾"));
        sortCityList.add(new SortCity("381", "LW", "25", "莱芜"));
        sortCityList.add(new SortCity("121", "LX", "6", "临夏"));
        sortCityList.add(new SortCity("520", "LX", "33", "潞西"));
        sortCityList.add(new SortCity("326", "LY", "19", "辽源"));
        sortCityList.add(new SortCity("169", "LY", "9", "贵阳"));
        sortCityList.add(new SortCity("343", "LY", "20", "辽阳"));
        sortCityList.add(new SortCity("101", "LY", "5", "龙岩"));
        sortCityList.add(new SortCity("238", "LY", "13", "洛阳"));
        sortCityList.add(new SortCity("291", "LYG", "17", "连云港"));
        sortCityList.add(new SortCity("114", "LZ", "6", "兰州"));
        sortCityList.add(new SortCity("430", "LZ", "28", "泸州"));
        sortCityList.add(new SortCity("504", "LZ", "32", "林芝"));
        sortCityList.add(new SortCity("163", "LZ", "8", "柳州"));
        sortCityList.add(new SortCity("224", "MDJ", "12", "牡丹江"));
        sortCityList.add(new SortCity("431", "MEK", "28", "马尔康"));
        sortCityList.add(new SortCity("29", "MH", "2", "闵行"));
        sortCityList.add(new SortCity("327", "MH", "19", "梅河"));
        sortCityList.add(new SortCity("454", "ML", "29", "苗栗"));
        sortCityList.add(new SortCity("140", "MM", "7", "茂名"));
        sortCityList.add(new SortCity("371", "MQ", "24", "玛沁"));
        sortCityList.add(new SortCity("433", "MS", "28", "眉山"));
        sortCityList.add(new SortCity("9", "MTG", "1", "门头沟"));
        sortCityList.add(new SortCity("17", "MY", "1", "密云"));
        sortCityList.add(new SortCity("370", "MY", "24", "海晏"));
        sortCityList.add(new SortCity("141", "MZ", "7", "梅州"));
        sortCityList.add(new SortCity("461", "MZ", "29", "马祖"));
        sortCityList.add(new SortCity("46", "NA", "3", "南岸"));
        sortCityList.add(new SortCity("93", "NAS", "4", "马鞍山"));
        sortCityList.add(new SortCity("541", "NB", "34", "宁波"));
        sortCityList.add(new SortCity("434", "NC", "28", "南充"));
        sortCityList.add(new SortCity("309", "NC", "18", "南昌"));
        sortCityList.add(new SortCity("78", "NC", "3", "南川"));
        sortCityList.add(new SortCity("103", "ND", "5", "宁德"));
        sortCityList.add(new SortCity("210", "NDH", "11", "南戴河"));
        sortCityList.add(new SortCity("36", "NH", "2", "南汇"));
        sortCityList.add(new SortCity("479", "NH", "30", "宁河"));
        sortCityList.add(new SortCity("283", "NJ", "17", "南京"));
        sortCityList.add(new SortCity("435", "NJ", "28", "内江"));
        sortCityList.add(new SortCity("471", "NK", "30", "南开"));
        sortCityList.add(new SortCity("156", "NN", "8", "南宁"));
        sortCityList.add(new SortCity("102", "NP", "5", "南平"));
        sortCityList.add(new SortCity("505", "NQ", "32", "那曲"));
        sortCityList.add(new SortCity("199", "NSQD", "10", "南沙群岛"));
        sortCityList.add(new SortCity("450", "NT", "29", "南投"));
        sortCityList.add(new SortCity("292", "NT", "17", "南通"));
        sortCityList.add(new SortCity("401", "NW", "26", "宁武"));
        sortCityList.add(new SortCity("239", "NY", "13", "南阳"));
        sortCityList.add(new SortCity("542", "OH", "34", "瓯海"));
        sortCityList.add(new SortCity("32", "PD", "2", "浦东"));
        sortCityList.add(new SortCity("449", "PD", "29", "屏东"));
        sortCityList.add(new SortCity("240", "PDS", "13", "平顶山"));
        sortCityList.add(new SortCity("15", "PG", "1", "平谷"));
        sortCityList.add(new SortCity("462", "PH", "29", "澎湖"));
        sortCityList.add(new SortCity("543", "PH", "34", "平湖"));
        sortCityList.add(new SortCity("344", "PJ", "20", "盘锦"));
        sortCityList.add(new SortCity("384", "PL", "25", "蓬莱"));
        sortCityList.add(new SortCity("122", "PL", "6", "平凉"));
        sortCityList.add(new SortCity("74", "PS", "3", "彭水"));
        sortCityList.add(new SortCity("104", "PT", "5", "莆田"));
        sortCityList.add(new SortCity("25", "PT", "2", "普陀"));
        sortCityList.add(new SortCity("317", "PX", "18", "萍乡"));
        sortCityList.add(new SortCity("241", "PY", "13", "濮阳"));
        sortCityList.add(new SortCity("293", "QD", "17", "启东"));
        sortCityList.add(new SortCity("385", "QD", "25", "青岛"));
        sortCityList.add(new SortCity("544", "QDH", "34", "千岛湖"));
        sortCityList.add(new SortCity("386", "QF", "25", "曲阜"));
        sortCityList.add(new SortCity("182", "QH", "10", "琼海"));
        sortCityList.add(new SortCity("211", "QHD", "11", "秦皇岛"));
        sortCityList.add(new SortCity("259", "QJ", "15", "潜江"));
        sortCityList.add(new SortCity("521", "QJ", "33", "曲靖"));
        sortCityList.add(new SortCity("52", "QJ", "3", "黔江"));
        sortCityList.add(new SortCity("54", "QJ", "3", "綦江"));
        sortCityList.add(new SortCity("35", "QP", "2", "青浦"));
        sortCityList.add(new SortCity("225", "QQHE", "12", "齐齐哈尔"));
        sortCityList.add(new SortCity("463", "QT", "29", "其它"));
        sortCityList.add(new SortCity("226", "QTH", "12", "七台河"));
        sortCityList.add(new SortCity("142", "QY", "7", "清远"));
        sortCityList.add(new SortCity("545", "QZ", "34", "衢州"));
        sortCityList.add(new SortCity("105", "QZ", "5", "泉州"));
        sortCityList.add(new SortCity("165", "QZ", "8", "钦州"));
        sortCityList.add(new SortCity("197", "QZLZ", "10", "琼中黎族"));
        sortCityList.add(new SortCity("547", "RA", "34", "瑞安"));
        sortCityList.add(new SortCity("58", "RC", "3", "荣昌"));
        sortCityList.add(new SortCity("506", "RKZ", "32", "日喀则"));
        sortCityList.add(new SortCity("387", "RZ", "25", "日照"));
        sortCityList.add(new SortCity("147", "SD", "7", "顺德"));
        sortCityList.add(new SortCity("145", "SG", "7", "韶关"));
        sortCityList.add(new SortCity("", "SH", "2", "上海"));
        sortCityList.add(new SortCity("497", "SHZ", "31", "石河子"));
        sortCityList.add(new SortCity("34", "SJ", "2", "松江"));
        sortCityList.add(new SortCity("49", "SJ", "3", "双挢"));
        sortCityList.add(new SortCity("7", "SJS", "1", "石景山"));
        sortCityList.add(new SortCity("201", "SJZ", "11", "石家庄"));
        sortCityList.add(new SortCity("106", "SM", "5", "三明"));
        sortCityList.add(new SortCity("522", "SM", "33", "思茅"));
        sortCityList.add(new SortCity("242", "SMX", "13", "三门峡"));
        sortCityList.add(new SortCity("437", "SN", "28", "遂宁"));
        sortCityList.add(new SortCity("507", "SN", "32", "山南"));
        sortCityList.add(new SortCity("329", "SP", "19", "四平"));
        sortCityList.add(new SortCity("44", "SPB", "3", "沙坪坝"));
        sortCityList.add(new SortCity("295", "SQ", "17", "宿迁"));
        sortCityList.add(new SortCity("243", "SQ", "13", "商丘"));
        sortCityList.add(new SortCity("318", "SR", "18", "上饶"));
        sortCityList.add(new SortCity("108", "SS", "5", "石狮"));
        sortCityList.add(new SortCity("143", "ST", "7", "汕头"));
        sortCityList.add(new SortCity("527", "ST", "33", "昭通"));
        sortCityList.add(new SortCity("144", "SW", "7", "汕尾"));
        sortCityList.add(new SortCity("107", "SW", "5", "邵武"));
        sortCityList.add(new SortCity("548", "SX", "34", "绍兴"));
        sortCityList.add(new SortCity("333", "SY", "20", "沈阳"));
        sortCityList.add(new SortCity("330", "SY", "19", "松原"));
        sortCityList.add(new SortCity("180", "SY", "10", "三亚"));
        sortCityList.add(new SortCity("276", "SY", "16", "邵阳"));
        sortCityList.add(new SortCity("12", "SY", "1", "顺义"));
        sortCityList.add(new SortCity("294", "SY", "17", "沭阳"));
        sortCityList.add(new SortCity("260", "SY", "15", "十堰"));
        sortCityList.add(new SortCity("227", "SYS", "12", "双鸭山"));
        sortCityList.add(new SortCity("296", "SZ", "17", "苏州"));
        sortCityList.add(new SortCity("412", "SZ", "27", "商州"));
        sortCityList.add(new SortCity("402", "SZ", "26", "朔州"));
        sortCityList.add(new SortCity("146", "SZ", "7", "深圳"));
        sortCityList.add(new SortCity("94", "SZ", "4", "宿州"));
        sortCityList.add(new SortCity("71", "SZ", "3", "石柱"));
        sortCityList.add(new SortCity("261", "SZ", "15", "随州"));
        sortCityList.add(new SortCity("363", "SZS", "23", "石嘴山"));
        sortCityList.add(new SortCity("388", "TA", "25", "泰安"));
        sortCityList.add(new SortCity("444", "TB", "29", "台北"));
        sortCityList.add(new SortCity("498", "TC", "31", "塔城"));
        sortCityList.add(new SortCity("297", "TC", "17", "太仓"));
        sortCityList.add(new SortCity("188", "TC", "10", "屯昌"));
        sortCityList.add(new SortCity("459", "TD", "29", "台东"));
        sortCityList.add(new SortCity("413", "TD", "27", "绥德"));
        sortCityList.add(new SortCity("476", "TG", "30", "塘沽"));
        sortCityList.add(new SortCity("228", "TH", "12", "绥化"));
        sortCityList.add(new SortCity("331", "TH", "19", "通化"));
        sortCityList.add(new SortCity("", "TJ", "30", "天津"));
        sortCityList.add(new SortCity("95", "TL", "4", "铜陵"));
        sortCityList.add(new SortCity("356", "TL", "22", "通辽"));
        sortCityList.add(new SortCity("345", "TL", "20", "铁岭"));
        sortCityList.add(new SortCity("56", "TL", "3", "铜梁"));
        sortCityList.add(new SortCity("299", "TL", "17", "同里"));
        sortCityList.add(new SortCity("499", "TLF", "31", "吐鲁番"));
        sortCityList.add(new SortCity("446", "TN", "29", "台南"));
        sortCityList.add(new SortCity("55", "TN", "3", "潼南"));
        sortCityList.add(new SortCity("372", "TR", "24", "同仁"));
        sortCityList.add(new SortCity("175", "TR", "9", "铜仁"));
        sortCityList.add(new SortCity("212", "TS", "11", "唐山"));
        sortCityList.add(new SortCity("123", "TS", "6", "天水"));
        sortCityList.add(new SortCity("96", "TX", "4", "屯溪"));
        sortCityList.add(new SortCity("11", "TY", "1", "通州"));
        sortCityList.add(new SortCity("394", "TY", "26", "太原"));
        sortCityList.add(new SortCity("457", "TY", "29", "桃园"));
        sortCityList.add(new SortCity("447", "TZ", "29", "台中"));
        sortCityList.add(new SortCity("298", "TZ", "17", "泰州"));
        sortCityList.add(new SortCity("414", "TZ", "27", "铜川"));
        sortCityList.add(new SortCity("550", "TZ", "34", "台州"));
        sortCityList.add(new SortCity("184", "WC", "10", "文昌"));
        sortCityList.add(new SortCity("438", "WC", "28", "汶川"));
        sortCityList.add(new SortCity("124", "WD", "6", "武都"));
        sortCityList.add(new SortCity("389", "WF", "25", "潍坊"));
        sortCityList.add(new SortCity("390", "WH", "25", "威海"));
        sortCityList.add(new SortCity("97", "WH", "4", "芜湖"));
        sortCityList.add(new SortCity("252", "WH", "15", "武汉"));
        sortCityList.add(new SortCity("357", "WH", "22", "乌海"));
        sortCityList.add(new SortCity("551", "WL", "34", "温岭"));
        sortCityList.add(new SortCity("64", "WL", "3", "武隆"));
        sortCityList.add(new SortCity("358", "WLHT", "22", "乌兰浩特"));
        sortCityList.add(new SortCity("483", "WLMQ", "31", "乌鲁木齐"));
        sortCityList.add(new SortCity("411", "WN", "27", "渭南"));
        sortCityList.add(new SortCity("191", "WN", "10", "万宁"));
        sortCityList.add(new SortCity("185", "WN", "10", "万宁"));
        sortCityList.add(new SortCity("474", "WQ", "30", "武清"));
        sortCityList.add(new SortCity("69", "WS", "3", "巫山"));
        sortCityList.add(new SortCity("523", "WS", "33", "文山"));
        sortCityList.add(new SortCity("48", "WS", "3", "万盛"));
        sortCityList.add(new SortCity("125", "WW", "6", "武威"));
        sortCityList.add(new SortCity("300", "WX", "17", "无锡"));
        sortCityList.add(new SortCity("70", "WX", "3", "巫溪"));
        sortCityList.add(new SortCity("262", "WX", "15", "武穴"));
        sortCityList.add(new SortCity("111", "WYS", "5", "武夷山"));
        sortCityList.add(new SortCity("166", "WZ", "8", "梧州"));
        sortCityList.add(new SortCity("552", "WZ", "34", "温州"));
        sortCityList.add(new SortCity("364", "WZ", "23", "吴忠"));
        sortCityList.add(new SortCity("39", "WZ", "3", "万州"));
        sortCityList.add(new SortCity("181", "WZS", "10", "五指山"));
        sortCityList.add(new SortCity("407", "XA", "27", "西安"));
        sortCityList.add(new SortCity("213", "XC", "11", "新城"));
        sortCityList.add(new SortCity("2", "XC", "1", "西城"));
        sortCityList.add(new SortCity("98", "XC", "4", "宣城"));
        sortCityList.add(new SortCity("246", "XC", "13", "许昌"));
        sortCityList.add(new SortCity("439", "XC", "28", "西昌"));
        sortCityList.add(new SortCity("266", "XF", "15", "襄樊"));
        sortCityList.add(new SortCity("126", "XF", "6", "西峰"));
        sortCityList.add(new SortCity("249", "XG", "14", "香港"));
        sortCityList.add(new SortCity("267", "XG", "15", "孝感"));
        sortCityList.add(new SortCity("22", "XH", "2", "徐汇"));
        sortCityList.add(new SortCity("251", "XJ", "14", "新界"));
        sortCityList.add(new SortCity("359", "XLHT", "22", "锡林浩特"));
        sortCityList.add(new SortCity("112", "XM", "5", "厦门"));
        sortCityList.add(new SortCity("264", "XN", "15", "咸宁"));
        sortCityList.add(new SortCity("365", "XN", "24", "西宁"));
        sortCityList.add(new SortCity("468", "XQ", "30", "西青"));
        sortCityList.add(new SortCity("72", "XS", "3", "秀山"));
        sortCityList.add(new SortCity("524", "XSBN", "33", "西双版纳"));
        sortCityList.add(new SortCity("198", "XSQD", "10", "西沙群岛"));
        sortCityList.add(new SortCity("277", "XT", "16", "湘潭"));
        sortCityList.add(new SortCity("263", "XT", "15", "仙桃"));
        sortCityList.add(new SortCity("214", "XT", "11", "邢台"));
        sortCityList.add(new SortCity("4", "XW", "1", "宣武"));
        sortCityList.add(new SortCity("244", "XX", "13", "新乡"));
        sortCityList.add(new SortCity("176", "XY", "9", "兴义"));
        sortCityList.add(new SortCity("245", "XY", "13", "信阳"));
        sortCityList.add(new SortCity("319", "XY", "18", "新余"));
        sortCityList.add(new SortCity("415", "XY", "27", "咸阳"));
        sortCityList.add(new SortCity("265", "XY", "15", "襄阳"));
        sortCityList.add(new SortCity("403", "XZ", "26", "忻州"));
        sortCityList.add(new SortCity("301", "XZ", "17", "徐州"));
        sortCityList.add(new SortCity("452", "XZ", "29", "新竹"));
        sortCityList.add(new SortCity("455", "Y", "29", "嘉义"));
        sortCityList.add(new SortCity("416", "YA", "27", "延安"));
        sortCityList.add(new SortCity("440", "YA", "28", "雅安"));
        sortCityList.add(new SortCity("110", "YA", "5", "永安"));
        sortCityList.add(new SortCity("441", "YB", "28", "宜宾"));
        sortCityList.add(new SortCity("50", "YB", "3", "渝北"));
        sortCityList.add(new SortCity("406", "YC", "26", "运城"));
        sortCityList.add(new SortCity("405", "YC", "26", "榆次"));
        sortCityList.add(new SortCity("268", "YC", "15", "宜昌"));
        sortCityList.add(new SortCity("302", "YC", "17", "盐城"));
        sortCityList.add(new SortCity("360", "YC", "23", "银川"));
        sortCityList.add(new SortCity("77", "YC", "3", "永川"));
        sortCityList.add(new SortCity("320", "YC", "18", "宜春"));
        sortCityList.add(new SortCity("229", "YC", "12", "伊春"));
        sortCityList.add(new SortCity("149", "YD", "7", "英德"));
        sortCityList.add(new SortCity("150", "YF", "7", "云浮"));
        sortCityList.add(new SortCity("332", "YJ", "19", "延吉"));
        sortCityList.add(new SortCity("148", "YJ", "7", "阳江"));
        sortCityList.add(new SortCity("346", "YK", "20", "营口"));
        sortCityList.add(new SortCity("417", "YL", "27", "榆林"));
        sortCityList.add(new SortCity("451", "YL", "29", "云林"));
        sortCityList.add(new SortCity("458", "YL", "29", "宜兰"));
        sortCityList.add(new SortCity("168", "YL", "8", "玉林"));
        sortCityList.add(new SortCity("500", "YN", "31", "伊宁"));
        sortCityList.add(new SortCity("177", "YP", "9", "玉屏"));
        sortCityList.add(new SortCity("28", "YP", "2", "杨浦"));
        sortCityList.add(new SortCity("18", "YQ", "1", "延庆"));
        sortCityList.add(new SortCity("404", "YQ", "26", "阳泉"));
        sortCityList.add(new SortCity("373", "YS", "24", "玉树"));
        sortCityList.add(new SortCity("321", "YT", "18", "鹰潭"));
        sortCityList.add(new SortCity("391", "YT", "25", "烟台"));
        sortCityList.add(new SortCity("525", "YX", "33", "玉溪"));
        sortCityList.add(new SortCity("304", "YX", "17", "宜兴"));
        sortCityList.add(new SortCity("553", "YY", "34", "余姚"));
        sortCityList.add(new SortCity("279", "YY", "16", "岳阳"));
        sortCityList.add(new SortCity("73", "YY", "3", "酉阳"));
        sortCityList.add(new SortCity("278", "YY", "16", "益阳"));
        sortCityList.add(new SortCity("67", "YY", "3", "云阳"));
        sortCityList.add(new SortCity("303", "YZ", "17", "扬州"));
        sortCityList.add(new SortCity("41", "YZ", "3", "渝中"));
        sortCityList.add(new SortCity("305", "YZ", "17", "仪征"));
        sortCityList.add(new SortCity("376", "YZ", "25", "兖州"));
        sortCityList.add(new SortCity("280", "YZ", "16", "永州"));
        sortCityList.add(new SortCity("26", "ZB", "2", "闸北"));
        sortCityList.add(new SortCity("151", "ZC", "7", "增城"));
        sortCityList.add(new SortCity("526", "ZD", "33", "中甸"));
        sortCityList.add(new SortCity("442", "ZG", "28", "自贡"));
        sortCityList.add(new SortCity("155", "ZH", "7", "珠海"));
        sortCityList.add(new SortCity("453", "ZH", "29", "彰化"));
        sortCityList.add(new SortCity("307", "ZJ", "17", "镇江"));
        sortCityList.add(new SortCity("152", "ZJ", "7", "湛江"));
        sortCityList.add(new SortCity("306", "ZJG", "17", "张家港"));
        sortCityList.add(new SortCity("281", "ZJJ", "16", "张家界"));
        sortCityList.add(new SortCity("38", "ZJJ", "2", "朱家角"));
        sortCityList.add(new SortCity("215", "ZJK", "11", "张家口"));
        sortCityList.add(new SortCity("247", "ZK", "13", "周口"));
        sortCityList.add(new SortCity("248", "ZMD", "13", "驻马店"));
        sortCityList.add(new SortCity("153", "ZQ", "7", "肇庆"));
        sortCityList.add(new SortCity("554", "ZS", "34", "舟山"));
        sortCityList.add(new SortCity("154", "ZS", "7", "中山"));
        sortCityList.add(new SortCity("200", "ZSQD", "10", "中沙群岛"));
        sortCityList.add(new SortCity("362", "ZW", "23", "中卫"));
        sortCityList.add(new SortCity("65", "ZX", "3", "忠县"));
        sortCityList.add(new SortCity("178", "ZY", "9", "遵义"));
        sortCityList.add(new SortCity("128", "ZY", "6", "张掖"));
        sortCityList.add(new SortCity("443", "ZY", "28", "资阳"));
        sortCityList.add(new SortCity("392", "ZZ", "25", "枣庄"));
        sortCityList.add(new SortCity("282", "ZZ", "16", "株洲"));
        sortCityList.add(new SortCity("230", "ZZ", "13", "郑州"));
        sortCityList.add(new SortCity("183", "ZZ", "10", "儋州"));
        sortCityList.add(new SortCity("308", "ZZ", "17", "周庄"));
        sortCityList.add(new SortCity("113", "ZZ", "5", "漳州"));
        return sortCityList;
    }

    public static List<SortCity> hotCityList = new ArrayList<>();

    public static List<SortCity> getHotCityList() {
        if (hotCityList.size() > 0) {
            return hotCityList;
        }
        hotCityList.add(new SortCity("", "BJ", "1", "北京", "101010100"));
        hotCityList.add(new SortCity("528", "HZ", "34", "杭州", "101210101"));
        hotCityList.add(new SortCity("", "TJ", "30", "天津", "101030100"));
        hotCityList.add(new SortCity("", "CQ", "3", "重庆", "101040100"));
        hotCityList.add(new SortCity("418", " CD", "28", "成都", "101270101"));
        return hotCityList;
    }

    public static final Map<String, String> province_map = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {
            put("0", "未填");
            put("1", "北京");
            put("2", "上海");
            put("3", "重庆");
            put("4", "安徽");
            put("5", "福建");
            put("6", "甘肃");
            put("7", "广东");
            put("8", "广西");
            put("9", "贵州");
            put("10", "海南");
            put("11", "河北");
            put("12", "黑龙江");
            put("13", "河南");
            put("14", "香港");
            put("15", "湖北");
            put("16", "湖南");
            put("17", "江苏");
            put("18", "江西");
            put("19", "吉林");
            put("20", "辽宁");
            put("21", "澳门");
            put("22", "内蒙");
            put("23", "宁夏");
            put("24", "青海");
            put("25", "山东");
            put("26", "山西");
            put("27", "陕西");
            put("28", "四川");
            put("29", "台湾");
            put("30", "天津");
            put("31", "新疆");
            put("32", "西藏");
            put("33", "云南");
            put("34", "浙江");
        }
    };

    public static final ArrayList<String> nextChangeLicenseDate = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add("1 年");
            add("2 年");
            add("3 年");
            add("4 年");
            add("5 年");
            add("6 年");
            add("7 年");
            add("8 年");
        }
    };

    public static final HashMap<String, Integer> nextChangeLicenseDate_hash = new HashMap<String, Integer>() {
        {
            put("1 年", 0);
            put("2 年", 1);
            put("3 年", 2);
            put("4 年", 3);
            put("5 年", 4);
            put("6 年", 5);
            put("7 年", 6);
            put("8 年", 7);
        }
    };

    public static final ArrayList<String> licenseType = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add("A1");
            add("A2");
            add("A3");
            add("B1");
            add("B2");
            add("C1");
            add("C2");
            add("C3");
        }
    };

    public static final HashMap<String, Integer> licenseTyoe_hash = new HashMap<String, Integer>() {
        {
            put("A1", 0);
            put("A2", 1);
            put("A3", 2);
            put("B1", 3);
            put("B2", 4);
            put("C1", 5);
            put("C2", 6);
            put("C3", 7);
        }
    };

    public static final ArrayList<String> baseTime = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add("00:00");
            add("00:30");
            add("01:00");
            add("01:30");
            add("02:00");
            add("02:30");
            add("03:00");
            add("03:30");
            add("04:00");
            add("04:30");
            add("05:00");
            add("05:30");
            add("06:00");
            add("06:30");
            add("07:00");
            add("07:30");
            add("08:00");
            add("08:30");
            add("09:00");
            add("09:30");
            add("10:00");
            add("10:30");
            add("11:00");
            add("11:30");
            add("12:00");
            add("12:30");
            add("13:00");
            add("13:30");
            add("14:00");
            add("14:30");
            add("15:00");
            add("15:30");
            add("16:00");
            add("16:30");
            add("17:00");
            add("17:30");
            add("18:00");
            add("18:30");
            add("19:00");
            add("19:30");
            add("20:00");
            add("20:30");
            add("21:00");
            add("21:30");
            add("22:00");
            add("22:30");
            add("23:00");
            add("23:30");
        }
    };

    public static ArrayList<String> getStartTime(String start, String end) {
        String up_time = start.substring(0, start.length() - 3);
        String down_time = end.substring(0, end.length() - 3);
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < baseTime.size(); i++) {
            if (down_time.compareTo(baseTime.get(i)) <= 0) {
                return res;
            }
            if (up_time.compareTo(baseTime.get(i)) <= 0) {
                res.add(baseTime.get(i));
            }
        }
        return res;
    }

    public static ArrayList<String> getEndTime(String start, String end) {
        String up_time = start.substring(0, start.length() - 3);
        String down_time = end.substring(0, end.length() - 3);
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < baseTime.size(); i++) {
            if (down_time.compareTo(baseTime.get(i)) < 0) {
                return res;
            }
            if (up_time.compareTo(baseTime.get(i)) < 0) {
                res.add(baseTime.get(i));
            }
        }
        return res;
    }

    public enum Maintain {
        EngineOil("机油", 0),
        EngineOilFilter("机油滤清器", 1),
        AirFilter("空气滤清器", 2),
        AirCondition("空调空气滤清器", 3),
        CoolingLiquid("冷却液", 4),
        OilFilter("油滤器", 5),
        FuelFilter("燃油滤清器", 6),
        SparkPlug("火花塞", 7),
        BrakeFluid("制动液", 8),
        ManualTransmission("手动变速箱油", 9),
        AutomaticTransmission("自动变速箱油", 10),
        EngineTimingBelt("发动机正时皮带", 11);

        public String desc;
        public int dex;

        Maintain(String desc, int dex) {
            this.desc = desc;
            this.dex = dex;
        }

        private static String getDesc(int dex) {
            for (Maintain m : Maintain.values()) {
                if (m.getDex() == dex) {
                    return m.desc;
                }
            }
            return null;
        }

        private int getDex() {
            return dex;
        }

        @Override
        public String toString() {
            return super.toString();
        }

        public static String getDescS(List<String> maintainItem) {
            String descS;
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("更换");
            for (String dex : maintainItem) {
                stringBuffer.append(getDesc(Integer.parseInt(dex))).append("、");
            }
            descS = stringBuffer.toString();
            descS = descS.substring(0, descS.length() - 1);
            LogUtils.logE("indexS : " + descS);
            return descS;
        }

        public static String getIndex(List<String> maintainItem) {
            if (maintainItem.size() <= 0)
                return "";
            String indexS;
            StringBuilder stringBuffer = new StringBuilder();
            for (String dex : maintainItem) {
                stringBuffer.append(dex).append(",");
            }
            indexS = stringBuffer.toString();
            indexS = indexS.substring(0, indexS.length() - 1);
            LogUtils.logE("indexS : " + indexS);
            return indexS;
        }
    }

    /**
     * 保存定位结果
     *
     * @param aMapLocation 高德地图定位结果
     */
    public static void saveLocation(AMapLocation aMapLocation) {
        SysModel.latitude = aMapLocation.getLatitude();
        SysModel.longitude = aMapLocation.getLongitude();
        if (SysModel.latitude != 0 || SysModel.latitude != 0) {
            PhoneSaveUtils.putDouble(AppConst.LATITUDE, (float) SysModel.latitude);
            PhoneSaveUtils.putDouble(AppConst.LONGITUDE, (float) SysModel.longitude);
            LogUtils.logE("定位位置=====>" + aMapLocation.getAddress());
            String city = aMapLocation.getCity();//城市信息
            String district = aMapLocation.getDistrict();//城区信息
            SortCity c = null;
            if (sortCityList.size() != 0) {
                if (!TextUtils.isEmpty(city)) {
                    c = findCityInCities(city);
                    if (c == null && !TextUtils.isEmpty(district)) {
                        c = findCityInCities(district);
                    }
                } else if (!TextUtils.isEmpty(district)) {
                    c = findCityInCities(district);
                }
                if (c == null) {
                    LogUtils.logE("位置比对结果=====>失败");
                } else {
                    LogUtils.logE("位置比对结果=====>" + c.toString());
                    SysModel.currentCity = c;
                }
                PhoneSaveUtils.putString(AppConst.CURRENT_CITY, SysModel.currentCity.getName());
                PhoneSaveUtils.putString(AppConst.CURRENT_CITY_CODE, SysModel.currentCity.getWeatherCode());
            }
        }
    }

    /**
     * 加载缓存的定位信息
     */
    public static void loadLocation() {
        SysModel.latitude = PhoneSaveUtils.getDouble(AppConst.LATITUDE, 0);
        SysModel.longitude = PhoneSaveUtils.getDouble(AppConst.LONGITUDE, 0);
        String city = PhoneSaveUtils.getString(AppConst.CURRENT_CITY, "");
        String cityCode = PhoneSaveUtils.getString(AppConst.CURRENT_CITY_CODE, "");
        SysModel.currentCity = new SortCity("", "", "", city, cityCode);
    }

    public static SortCity findCityInCities(String city) {
        for (SortCity c : sortCityList) {
            if (city.contains(c.getName()) || (city.charAt(0) + "" +
                    city.charAt(city.length() - 1)).equals(c.getName())) {
                return c;
            }
        }
        return null;
    }
}
