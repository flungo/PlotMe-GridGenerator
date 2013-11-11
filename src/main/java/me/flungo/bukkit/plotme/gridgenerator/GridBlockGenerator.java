package me.flungo.bukkit.plotme.gridgenerator;

import java.util.Random;
import me.flungo.bukkit.plotme.abstractgenerator.WorldGenConfig;
import static me.flungo.bukkit.plotme.gridgenerator.GridWorldConfigPath.BASE_BLOCK;
import static me.flungo.bukkit.plotme.gridgenerator.GridWorldConfigPath.FILL_BLOCK;
import static me.flungo.bukkit.plotme.gridgenerator.GridWorldConfigPath.GROUND_LEVEL;
import static me.flungo.bukkit.plotme.gridgenerator.GridWorldConfigPath.PLOT_FLOOR_BLOCK;
import static me.flungo.bukkit.plotme.gridgenerator.GridWorldConfigPath.PLOT_SIZE;
import static me.flungo.bukkit.plotme.gridgenerator.GridWorldConfigPath.WALL_BLOCK;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class GridBlockGenerator extends BlockPopulator {

    private final WorldGenConfig wgc;

    public GridBlockGenerator(WorldGenConfig wgc) {
        this.wgc = wgc;
    }

    @Override
    public void populate(World w, Random rand, Chunk chunk) {
        final int plotsize = wgc.getInt(PLOT_SIZE);
        final int roadheight = wgc.getInt(GROUND_LEVEL);
        final byte bottom = wgc.getBlockRepresentation(BASE_BLOCK).getData();
        final byte wall = wgc.getBlockRepresentation(WALL_BLOCK).getData();
        final byte plotfloor = wgc.getBlockRepresentation(PLOT_FLOOR_BLOCK).getData();
        final byte filling = wgc.getBlockRepresentation(FILL_BLOCK).getData();

        int cx = chunk.getX();
        int cz = chunk.getZ();

        int xx = cx << 4;
        int zz = cz << 4;

        double size = plotsize;
        int valx;
        int valz;

        double n1 = -1;
        double n2 = 0;
        double n3 = 1;
        int mod2 = 0;
        int mod1 = 1;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = roadheight + 2;

                w.setBiome(x + xx, z + zz, Biome.PLAINS);

                valx = (cx * 16 + x);
                valz = (cz * 16 + z);

                for (int y = 0; y < height; y++) {
                    if (y == 0) {
                        //result[(x * 16 + z) * 128 + y] = bottom;
                        setBlock(w, x + xx, y, z + zz, bottom);

                    } else if (y == roadheight) {
                        if ((valx - n3 + mod1) % size == 0 || (valx + n3 + mod2) % size == 0) //middle+3
                        {
                            boolean found = false;
                            for (double i = n2; i >= 0; i--) {
                                if ((valz - i + mod1) % size == 0 || (valz + i + mod2) % size == 0) {
                                    found = true;
                                    break;
                                }
                            }

                            if (found) {
                                //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                setBlock(w, x + xx, y, z + zz, plotfloor);
                            } else {
                                //result[(x * 16 + z) * 128 + y] = filling; //filling
                                setBlock(w, x + xx, y, z + zz, filling);
                            }
                        } else if ((valx - n2 + mod1) % size == 0 || (valx + n2 + mod2) % size == 0) //middle+2
                        {
                            if ((valz - n3 + mod1) % size == 0 || (valz + n3 + mod2) % size == 0
                                    || (valz - n2 + mod1) % size == 0 || (valz + n2 + mod2) % size == 0) {
                                //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                setBlock(w, x + xx, y, z + zz, plotfloor);
                            } else {
                                //result[(x * 16 + z) * 128 + y] = floor2; //floor2
                                setBlock(w, x + xx, y, z + zz, plotfloor);
                            }
                        } else if ((valx - n1 + mod1) % size == 0 || (valx + n1 + mod2) % size == 0) //middle+2
                        {
                            if ((valz - n2 + mod1) % size == 0 || (valz + n2 + mod2) % size == 0
                                    || (valz - n1 + mod1) % size == 0 || (valz + n1 + mod2) % size == 0) {
                                //result[(x * 16 + z) * 128 + y] = floor2; //floor2
                                setBlock(w, x + xx, y, z + zz, plotfloor);
                            } else {
                                //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                setBlock(w, x + xx, y, z + zz, plotfloor);
                            }
                        } else {
                            boolean found = false;
                            for (double i = n1; i >= 0; i--) {
                                if ((valz - i + mod1) % size == 0 || (valz + i + mod2) % size == 0) {
                                    found = true;
                                    break;
                                }
                            }

                            if (found) {
                                //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                setBlock(w, x + xx, y, z + zz, plotfloor);
                            } else {
                                if ((valz - n2 + mod1) % size == 0 || (valz + n2 + mod2) % size == 0) {
                                    //result[(x * 16 + z) * 128 + y] = floor2; //floor2
                                    setBlock(w, x + xx, y, z + zz, plotfloor);
                                } else {
                                    boolean found2 = false;
                                    for (double i = n1; i >= 0; i--) {
                                        if ((valz - i + mod1) % size == 0 || (valz + i + mod2) % size == 0) {
                                            found2 = true;
                                            break;
                                        }
                                    }

                                    if (found2) {
                                        //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                        setBlock(w, x + xx, y, z + zz, plotfloor);
                                    } else {
                                        boolean found3 = false;
                                        for (double i = n3; i >= 0; i--) {
                                            if ((valx - i + mod1) % size == 0 || (valx + i + mod2) % size == 0) {
                                                found3 = true;
                                                break;
                                            }
                                        }

                                        if (found3) {
                                            //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                            setBlock(w, x + xx, y, z + zz, plotfloor);
                                        } else {
                                            //result[(x * 16 + z) * 128 + y] = plotfloor; //plotfloor
                                            setBlock(w, x + xx, y, z + zz, plotfloor);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (y == (roadheight + 1)) {

                        if ((valx - n3 + mod1) % size == 0 || (valx + n3 + mod2) % size == 0) //middle+3
                        {
                            boolean found = false;
                            for (double i = n2; i >= 0; i--) {
                                if ((valz - i + mod1) % size == 0 || (valz + i + mod2) % size == 0) {
                                    found = true;
                                    break;
                                }
                            }

                            if (found) {
                                //result[(x * 16 + z) * 128 + y] = air;
                                //setBlock(result, x, y, z, air);
                            } else {
                                //result[(x * 16 + z) * 128 + y] = wall;
                                setBlock(w, x + xx, y, z + zz, wall);
                            }
                        } else {
                            boolean found = false;
                            for (double i = n2; i >= 0; i--) {
                                if ((valx - i + mod1) % size == 0 || (valx + i + mod2) % size == 0) {
                                    found = true;
                                    break;
                                }
                            }

                            if (found) {
                                //result[(x * 16 + z) * 128 + y] = air;
                                //setBlock(result, x, y, z, air);
                            } else {
                                if ((valz - n3 + mod1) % size == 0 || (valz + n3 + mod2) % size == 0) {
                                    //result[(x * 16 + z) * 128 + y] = wall;
                                    setBlock(w, x + xx, y, z + zz, wall);
                                } else {
                                    //result[(x * 16 + z) * 128 + y] = air;
                                    //setBlock(result, x, y, z, air);
                                }
                            }
                        }
                    } else {
                        //result[(x * 16 + z) * 128 + y] = filling;
                        setBlock(w, x + xx, y, z + zz, filling);
                    }
                }
            }
        }
    }

    private void setBlock(World w, int x, int y, int z, byte val) {
        if (val != 0) {
            w.getBlockAt(x, y, z).setData(val);
        }
    }

}
