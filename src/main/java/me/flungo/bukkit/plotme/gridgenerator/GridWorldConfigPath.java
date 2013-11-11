/*
 * Copyright (C) 2013 Fabrizio Lungo <fab@lungo.co.uk> - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Fabrizio Lungo <fab@lungo.co.uk>, November 2013
 */
package me.flungo.bukkit.plotme.gridgenerator;

import me.flungo.bukkit.plotme.abstractgenerator.AbstractWorldConfigPath;
import me.flungo.bukkit.plotme.abstractgenerator.WorldConfigPath;

/**
 *
 * @author Fabrizio Lungo <fab@lungo.co.uk>
 */
public enum GridWorldConfigPath implements WorldConfigPath {

    PLOT_SIZE(AbstractWorldConfigPath.PLOT_SIZE),
    X_TRANSLATION(AbstractWorldConfigPath.X_TRANSLATION),
    Z_TRANSLATION(AbstractWorldConfigPath.Z_TRANSLATION),
    BASE_BLOCK(AbstractWorldConfigPath.BASE_BLOCK),
    GROUND_LEVEL(AbstractWorldConfigPath.GROUND_LEVEL);

    public final String path;
    public final Object def;

    private GridWorldConfigPath(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    private GridWorldConfigPath(AbstractWorldConfigPath awcp) {
        this.path = awcp.path;
        this.def = awcp.def;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public Object def() {
        return def;
    }
}
