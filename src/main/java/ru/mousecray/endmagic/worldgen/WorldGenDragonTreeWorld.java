package ru.mousecray.endmagic.worldgen;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.minecraft.init.Blocks.END_STONE;

public class WorldGenDragonTreeWorld {
    private WorldGenDragonTree generator = new WorldGenDragonTree(true);

    private int centralIslandSize = 30;

    public void generateWorld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (chunkX * chunkX + chunkZ * chunkZ < centralIslandSize * centralIslandSize) {
            if (!world.getChunkFromChunkCoords(chunkX, chunkZ).isEmpty()) {
                findVerticalSlope(world, chunkX, chunkZ).forEach(place -> generator.generate(world, random, place));
            }
        }
    }

    private Stream<BlockPos> findVerticalSlope(World world, int chunkX, int chunkZ) {
        int ymin = 30;
        int ymax = 50;

        return IntStream.rangeClosed(ymin, ymax).mapToObj(y ->
                IntStream.rangeClosed(5, 11).mapToObj(x ->
                        IntStream.rangeClosed(5, 11).mapToObj(z ->
                                new BlockPos(x + (chunkX << 4), y, z + (chunkZ << 4))))
                        .flatMap(Function.identity())
                        .filter(world::isAirBlock).flatMap(startPos ->
                        {
                            Stream<BlockPos> blockPosStream = Arrays.stream(EnumFacing.HORIZONTALS)
                                    .flatMap(side -> walk(world, startPos.toImmutable(), side))
                                    .filter(pos -> checkFreePlane(world, pos.getLeft(), pos.getRight()))
                                    .flatMap(i ->
                                            Stream.of(
                                                    i.getLeft().offset(i.getRight())
                                            )
                                    );
                            return blockPosStream;
                        }
                )
        ).flatMap(Function.identity());
    }

    private boolean checkFreePlane(World world, BlockPos pos, EnumFacing air) {
        EnumFacing side = air.rotateY();
        return IntStream.rangeClosed(-1, 1).mapToObj(i ->
                IntStream.rangeClosed(-2, 2).mapToObj(j -> {
                    BlockPos current = pos.offset(side, i).offset(EnumFacing.UP, j);
                    return world.getBlockState(current).getBlock() != END_STONE || !world.isAirBlock(current.offset(air));
                })
        ).flatMap(Function.identity())
                .noneMatch(i -> i);
    }

    public static Stream<Pair<BlockPos, EnumFacing>> walk(World world, BlockPos start, EnumFacing to) {
        int maxX = (start.getX() >> 4 << 4) + 16;
        int maxZ = (start.getZ() >> 4 << 4) + 16;

        BlockPos current = start;
        while (world.getBlockState(current).getBlock() != END_STONE)
            if (current.getX() < maxX && current.getZ() < maxZ)
                current = current.offset(to);
            else
                return Stream.empty();

        return Stream.of(Pair.of(current, to.getOpposite()));
    }
}
