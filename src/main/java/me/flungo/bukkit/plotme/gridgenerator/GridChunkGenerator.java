package me.flungo.bukkit.plotme.gridgenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.flungo.bukkit.plotme.abstractgenerator.AbstractChunkGenerator;
import me.flungo.bukkit.plotme.abstractgenerator.WorldGenConfig;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class GridChunkGenerator extends AbstractChunkGenerator {

    private final WorldGenConfig wgc;
    private final GridGenerator plugin;

    public GridChunkGenerator(GridGenerator instance, WorldGenConfig wgc) {
        super(instance, wgc);

        plugin = instance;
        this.wgc = wgc;
    }

    @Override
    public short[][] generateExtBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes) {
        final int maxY = world.getMaxHeight();

        final int plotsize = wgc.getInt("PlotSize");
        final int pathsize = wgc.getInt("PathSize");
        final int roadheight = wgc.getInt("RoadHeight");
        final short bottom = wgc.getBlockRepresentation("BottomBlock").getId();
        final short wall = wgc.getBlockRepresentation("BottomBlock").getId();
        final short plotfloor = wgc.getBlockRepresentation("BottomBlock").getId();
        final short filling = wgc.getBlockRepresentation("BottomBlock").getId();
        final short floor1 = wgc.getBlockRepresentation("BottomBlock").getId();
        final short floor2 = wgc.getBlockRepresentation("BottomBlock").getId();

        short[][] result = new short[maxY / 16][];

        double size = plotsize + pathsize;
        int valx;
        int valz;

        double n1;
        double n2;
        double n3;
        int mod2 = 0;
        int mod1 = 1;

        if (pathsize % 2 == 1) {
            n1 = Math.ceil(((double) pathsize) / 2) - 2;
            n2 = Math.ceil(((double) pathsize) / 2) - 1;
            n3 = Math.ceil(((double) pathsize) / 2);
        } else {
            n1 = Math.floor(((double) pathsize) / 2) - 2;
            n2 = Math.floor(((double) pathsize) / 2) - 1;
            n3 = Math.floor(((double) pathsize) / 2);
        }

        if (pathsize % 2 == 1) {
            mod2 = -1;
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = roadheight + 2;

                for (int y = 0; y < height; y++) {
                    valx = (cx * 16 + x);
                    valz = (cz * 16 + z);

                    if (y == 0) {
                        //result[(x * 16 + z) * 128 + y] = bottom;
                        setBlock(result, x, y, z, bottom);

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
                                setBlock(result, x, y, z, floor1);
                            } else {
                                //result[(x * 16 + z) * 128 + y] = filling; //filling
                                setBlock(result, x, y, z, filling);
                            }
                        } else if ((valx - n2 + mod1) % size == 0 || (valx + n2 + mod2) % size == 0) //middle+2
                        {
                            if ((valz - n3 + mod1) % size == 0 || (valz + n3 + mod2) % size == 0
                                    || (valz - n2 + mod1) % size == 0 || (valz + n2 + mod2) % size == 0) {
                                //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                setBlock(result, x, y, z, floor1);
                            } else {
                                //result[(x * 16 + z) * 128 + y] = floor2; //floor2
                                setBlock(result, x, y, z, floor2);
                            }
                        } else if ((valx - n1 + mod1) % size == 0 || (valx + n1 + mod2) % size == 0) //middle+2
                        {
                            if ((valz - n2 + mod1) % size == 0 || (valz + n2 + mod2) % size == 0
                                    || (valz - n1 + mod1) % size == 0 || (valz + n1 + mod2) % size == 0) {
                                //result[(x * 16 + z) * 128 + y] = floor2; //floor2
                                setBlock(result, x, y, z, floor2);
                            } else {
                                //result[(x * 16 + z) * 128 + y] = floor1; //floor1
                                setBlock(result, x, y, z, floor1);
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
                                setBlock(result, x, y, z, floor1);
                            } else {
                                if ((valz - n2 + mod1) % size == 0 || (valz + n2 + mod2) % size == 0) {
                                    //result[(x * 16 + z) * 128 + y] = floor2; //floor2
                                    setBlock(result, x, y, z, floor2);
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
                                        setBlock(result, x, y, z, floor1);
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
                                            setBlock(result, x, y, z, floor1);
                                        } else {
                                            //result[(x * 16 + z) * 128 + y] = plotfloor; //plotfloor
                                            setBlock(result, x, y, z, plotfloor);
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
                                setBlock(result, x, y, z, wall);
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
                                    setBlock(result, x, y, z, wall);
                                } else {
                                    //result[(x * 16 + z) * 128 + y] = air;
                                    //setBlock(result, x, y, z, air);
                                }
                            }
                        }
                    } else {
                        //result[(x * 16 + z) * 128 + y] = filling;
                        setBlock(result, x, y, z, filling);
                    }
                }
            }
        }

        return result;
    }

    private void setBlock(short[][] result, int x, int y, int z, short blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new short[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator) new GridBlockGenerator(wgc));
    }

}
