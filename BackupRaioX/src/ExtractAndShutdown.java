import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExtractAndShutdown {

    public static void main(String[] args) {
        // Caminho do arquivo compactado
        String arquivoCompactado = "caminho/do/seu/arquivo.zip";

        // Caminho para extrair o arquivo
        String caminhoExtracao = "caminho/de/extracao";

        // Extrair o arquivo
        extrairArquivo(arquivoCompactado, caminhoExtracao);

        // Desligar o sistema
        desligarSistema();
    }

    public static void extrairArquivo(String arquivoCompactado, String caminhoExtracao) {
        try (ZipArchiveInputStream zis = new ZipArchiveInputStream(new FileInputStream(arquivoCompactado))) {
            ArchiveEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File entryFile = new File(caminhoExtracao, entry.getName());
                    if (!entryFile.getParentFile().exists()) {
                        Files.createDirectories(entryFile.getParentFile().toPath());
                    }
                    try (OutputStream os = new FileOutputStream(entryFile)) {
                        IOUtils.copy(zis, os);
                    }
                }
            }
            System.out.println("Arquivo extraído com sucesso para: " + caminhoExtracao);
        } catch (IOException e) {
            System.err.println("Erro ao extrair o arquivo: " + e.getMessage());
        }
    }

    public static void desligarSistema() {
        try {
            // Executar o comando de desligamento
            Process processo = Runtime.getRuntime().exec("shutdown -s -t 0");
            int resultado = processo.waitFor();
            if (resultado == 0) {
                System.out.println("Sistema desligado com sucesso.");
            } else {
                System.err.println("Erro ao desligar o sistema.");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar o comando de desligamento: " + e.getMessage());
        }
    }
}
