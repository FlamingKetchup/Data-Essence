package com.cmdpro.datanessence.api;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class MultiFluidTankNoDuplicateFluids extends MultiFluidTank {
    List<FluidTank> tanks;
    public MultiFluidTankNoDuplicateFluids(List<FluidTank> tanks) {
        super(tanks);
        this.tanks = tanks;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        FluidStack remaining = resource.copy();
        for (FluidTank i : tanks) {
            if (i.getFluid().getAmount() >= i.getCapacity()) {
                if (FluidStack.isSameFluidSameComponents(i.getFluid(), resource)) {
                    break;
                }
            }
            remaining.setAmount(remaining.getAmount()-i.fill(remaining, action));
            if (remaining.getAmount() <= 0) {
                break;
            }
        }
        return resource.getAmount()-remaining.getAmount();
    }
}
