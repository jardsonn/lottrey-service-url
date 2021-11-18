package com.jcs.lottery;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;

public class APIServiceUrl {
    private static final String URL_BASE = "http://loterias.caixa.gov.br/wps/portal/loterias/landing";
    private static final String CSS_SELECTOR_BASE = "base";
    private static final String ATTRIBUTE_KEY_HREF = "href";
    private static final String SEARCH_INPUT_ID = "urlBuscarResultado";
    private static final String PARAMS_CONTEST = "p=concurso=%d";

    private static final String MEGA_SENA = "/megasena";
    private static final String LOTOFACIL = "/lotofacil";
    private static final String LOTOMAINA = "/lotomania";
    private static final String QUINA = "/quina";

    private Connection getConnection(String params) {
        return Jsoup.connect(URL_BASE + (params == null ? "/megasena" : params)).timeout(1000 * 1000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36 Edg/95.0.1020.53");
    }

    public String getUrlApiLotofacil(int concurso) {
        return getApiUrl(LOTOFACIL, concurso);
    }

    public String getUrlApiLotofacil() {
        return getApiUrl(LOTOFACIL, 0);
    }

    public String getUrlApiMegaSena(int concurso) {
        return getApiUrl(MEGA_SENA, concurso);
    }

    public String getUrlApiMegaSena() {
        return getApiUrl(MEGA_SENA, 0);
    }

    public String getUrlApiLotomania() {
        return getApiUrl(LOTOMAINA, 0);
    }

    public String getUrlApiLotomania(int concurso) {
        return getApiUrl(LOTOMAINA, concurso);
    }

    public String getUrlApiQuina(int concurso) {
        return getApiUrl(QUINA, concurso);
    }

    public String getUrlApiQuina() {
        return getApiUrl(QUINA, 0);
    }


    private String getApiUrl(String params, int concurso) {
        try {
            Connection lotofacilConnection = getConnection(params);
            Document doc = lotofacilConnection.get();
            int statusCode = lotofacilConnection.execute().statusCode();
            String apiUrl = getExtractedUrl(doc);
            if (statusCode == 200) {
                if (concurso > 0)
                    return apiUrl.substring(0, apiUrl.length() - 2) + String.format(PARAMS_CONTEST, concurso);
                else
                    return apiUrl;
            } else {
                return "something happened wrong code: " + statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getExtractedUrl(Document doc) {
        String urlBaseLottery = doc.select(CSS_SELECTOR_BASE).attr(ATTRIBUTE_KEY_HREF);
        String urlSearchResult = Objects.requireNonNull(doc.getElementById(SEARCH_INPUT_ID)).val();
        return urlBaseLottery + urlSearchResult;
    }
}
