package util;

public enum EnumRegex {
    MOV("(mov)\\s+(0x[A-F0-9]+),\\s+([A-D]|\\d+)|(mov)\\s+([A-D]),\\s+(0x[A-F0-9]+)"),
    ADD("(add)\\s+([A-D]|0x[A-F0-9]+),\\s+([A-D]|PI|\\d+)\\b"),
    INC("(inc)\\s+([A-D]|0x[A-F0-9]+)"),
    IMUL("(imul)\\s+([A-D]),\\s+([A-D]),\\s+([A-D]|\\d+)|(imul)\\s+(0x[A-F0-9]+),\\s+([A-D]|\\d+),\\s+([A-D]|\\d+)");

    private String modelRegex;

    EnumRegex(String urlRegex){
        this.modelRegex = urlRegex;
    }
    public String getRegex(){

        return modelRegex;
    }


}
