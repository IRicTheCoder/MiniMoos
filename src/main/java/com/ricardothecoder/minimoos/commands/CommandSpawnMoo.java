package com.ricardothecoder.minimoos.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.ricardothecoder.minimoos.entities.EntityFluidMoo;
import com.ricardothecoder.yac.fluids.FluidManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CommandSpawnMoo extends CommandBase
{
	@Override
    public String getCommandName()
    {
        return "spawnmoo";
    }

	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.spawnmoo.usage";
    }

	@Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.spawnmoo.usage", new Object[0]);
        }
        else
        {
            String s = args[0];
            BlockPos blockpos = sender.getPosition();
            Vec3d vec3d = sender.getPositionVector();
            double d0 = vec3d.xCoord;
            double d1 = vec3d.yCoord;
            double d2 = vec3d.zCoord;

            if (args.length >= 4)
            {
                d0 = parseDouble(d0, args[1], true);
                d1 = parseDouble(d1, args[2], false);
                d2 = parseDouble(d2, args[3], true);
                blockpos = new BlockPos(d0, d1, d2);
            }
            
            if (FluidRegistry.getFluid(s) == null || !Arrays.asList(FluidManager.getContainableFluids()).contains(FluidRegistry.getFluid(s)))
            	throw new CommandException("commands.spawnmoo.failed", new Object[0]);

            World world = sender.getEntityWorld();

            if (!world.isBlockLoaded(blockpos))
            {
                throw new CommandException("commands.spawnmoo.outOfWorld", new Object[0]);
            }
            else
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                
                nbttagcompound.setString("id", "minimoos.fluidmoo");
                nbttagcompound.setBoolean("isFool", false);
                
                nbttagcompound.setString(EntityFluidMoo.FLUID_NAME_ID, s);
                nbttagcompound.setInteger(EntityFluidMoo.BUCKET_AMOUNT_ID, 1);
                nbttagcompound.setInteger(EntityFluidMoo.DELAY_ID, 0);
        		nbttagcompound.setInteger(EntityFluidMoo.QUANTITY_ID, 1);
        		nbttagcompound.setInteger(EntityFluidMoo.EFFICIENCY_ID, 1);
                
                Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, true);

                if (entity == null)
                {
                    throw new CommandException("commands.spawnmoo.failed", new Object[0]);
                }
                else
                {
                    entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);

                    notifyCommandListener(sender, this, "commands.spawnmoo.success", new Object[0]);
                }
            }
        }
    }

	@Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
		List<String> names = new ArrayList<String>();
		for (Fluid f : FluidManager.getContainableFluids())
			names.add(f.getName());
		
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, names) : (args.length > 1 && args.length <= 4 ? getTabCompletionCoordinate(args, 1, pos) : Collections.<String>emptyList());
    }
}