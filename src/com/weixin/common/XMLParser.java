package com.weixin.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {

    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  Util.getStringStream(xmlString);
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < allNodes.getLength(); i++) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
        }
        return map;
    }

    public static String node(String node, String value, boolean isCDATA){
        StringBuffer sb = new StringBuffer("<");
        sb.append(node.trim());
        sb.append(">");
        if(isCDATA){
            sb.append("<![CDATA[");
            sb.append(value);
            sb.append("]]>");
        }else{
            sb.append(value);
        }
        sb.append("</");
        sb.append(node.trim());
        sb.append(">");
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public static String getXmlStrFromMap(Map<String, Object> map){
        StringBuffer sb = new StringBuffer("<xml>");
        for(Map.Entry<String, Object> entry : map.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                sb.append(node(key, value.toString(), true));
            }else if(value instanceof Number || value instanceof Boolean){
                sb.append(node(key, String.valueOf(value), false));
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

}