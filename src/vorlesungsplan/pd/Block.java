package vorlesungsplan.pd;

public class Block {
    String blockDay;
    int block_nr;
    Modul modul;

    public Block() {
    }

    public Block(Integer block_nr, String blockDay, Modul modul) {
        this.block_nr = block_nr;
        this.blockDay = blockDay;
        this.modul = modul;
    }

    public String getBlockDay() {
        return blockDay;
    }

    public void setBlockDay(String blockDay) {
        this.blockDay = blockDay;
    }

    public int getBlock_nr() {
        return block_nr;
    }

    public void setBlock_nr(int block_nr) {
        this.block_nr = block_nr;
    }

    public Modul getModul() {
        return modul;
    }

    public void setModul(Modul modul) {
        this.modul = modul;
    }
}
