package com.alone.hotel.controller.superadmin;

import com.alone.hotel.dto.PositionExecution;
import com.alone.hotel.entity.Position;
import com.alone.hotel.enums.ResultEnum;
import com.alone.hotel.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.controller.superadmin
 * @Author: Alone
 * @CreateTime: 2020-03-13 15:10
 * @Description:
 */

@RestController
@RequestMapping("/superadmin")
public class PositionManagement {
    @Autowired
    private PositionService positionService;

    @PostMapping("/addposition")
    private PositionExecution addPosition(@RequestParam("position") Position position){
        if(position != null && position.getPositionId() != null){
            try{
                PositionExecution pe = positionService.insertPosition(position);
                if(pe.getState() == ResultEnum.SUCCESS.getState()){
                    return new PositionExecution(ResultEnum.SUCCESS);
                } else {
                    return new PositionExecution(ResultEnum.INNER_ERROR);
                }
            } catch (Exception e){
                return new PositionExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new PositionExecution(ResultEnum.EMPTY);
        }
    }

    @GetMapping("/querypositionbyid")
    private PositionExecution queryPositionById(@RequestParam("positionId")int positionId){
        PositionExecution positionExecution = null;
        if(positionId > 0){
            positionExecution = positionService.queryPositionById(positionId);
        } else {
            positionExecution = new PositionExecution(ResultEnum.ID_ERROR);
        }
        return positionExecution;
    }

    @GetMapping("/querypositionlist")
    private PositionExecution queryPositionList(@RequestParam("positionName")String positionName, @RequestParam("positionNote")String positionNote,
                                       @RequestParam("pageIndex")int pageIndex, @RequestParam("pageSize")int pageSize){
        Position positionCondition = new Position();
        positionCondition.setPositionName(positionName);
        positionCondition.setPositionNote(positionNote);
        if(pageIndex > 0 && pageSize > 0){
            try{
                PositionExecution pe = positionService.queryPositionList(positionCondition, pageIndex, pageSize);
                return pe;
            } catch (Exception e){
                return new PositionExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new PositionExecution(ResultEnum.PAGE_ERROR);
        }
    }

    @PostMapping("/modifyposition")
    private PositionExecution modifyPosition(@RequestParam("position")Position position){
        if(position != null && position.getPositionId() != null){
            try{
                PositionExecution positionExecution = positionService.modifyPosition(position);
                return positionExecution;
            } catch (Exception e){
                return new PositionExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new PositionExecution(ResultEnum.EMPTY);
        }
    }

    @PostMapping("/deleteposition")
    private PositionExecution deletePosition(@RequestParam("positionId")int positionId){
        if(positionId > 0){
            try{
                PositionExecution positionExecution = positionService.deletePosition(positionId);
                return positionExecution;
            } catch (Exception e){
                return new PositionExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new PositionExecution(ResultEnum.ID_ERROR);
        }
    }
}
