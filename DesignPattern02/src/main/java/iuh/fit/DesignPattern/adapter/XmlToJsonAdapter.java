package iuh.fit.DesignPattern.adapter;

public class XmlToJsonAdapter implements JsonService {

    private final XmlService xmlService;

    public XmlToJsonAdapter(XmlService xmlService) {
        this.xmlService = xmlService;
    }

    @Override
    public void sendJson(String jsonData) {
        String xmlData = convertJsonToXml(jsonData);
        xmlService.sendXml(xmlData);
    }

    private String convertJsonToXml(String json) {
        return "<data>" + json + "</data>";
    }
}

