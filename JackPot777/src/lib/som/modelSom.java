package lib.som;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class modelSom {

    private Clip clipComeco; // Variável para a música de começo
    private static final String Comeco = "D:\\Projetos Completos\\Projeto JackPot777\\JackPot777\\src\\lib\\som\\somMaquina.wav";

    public void play() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(Comeco).getAbsoluteFile());
            clipComeco = AudioSystem.getClip(); // Cria a instância do Clip para a música de começo
            clipComeco.open(audioInputStream);
            clipComeco.loop(Clip.LOOP_CONTINUOUSLY); // Define o loop como infinito
        } catch (Exception ex) {
            System.out.println("Erro ao executar SOM!" + ex);
        }
    }

    // Método para parar a música de começo
    public void stop() {
        if (clipComeco != null && clipComeco.isRunning()) {
            clipComeco.stop();
            clipComeco.close();
        }
    }
}
