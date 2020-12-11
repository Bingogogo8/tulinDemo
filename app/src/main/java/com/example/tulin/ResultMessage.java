package com.example.tulin;

import java.util.List;

import javax.xml.transform.Result;

public class ResultMessage {
private List<Results> results ;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public  class Results {
        private  String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
