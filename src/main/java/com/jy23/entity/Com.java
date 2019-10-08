package com.jy23.entity ;

import java.lang.String;
import java.lang.Integer;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Com {
	private Integer comId;				//
	private String comName;				//
	private String comBautrate;			//
	private String comEnable;			//
	private Integer comState;			//
	private String comJyw;				//
	private String comTzw;				//
	private String comSjw;				//
	private String comLkz;				//
	private Integer comMode;				//
	private Integer comPort;			//
	private Integer comCollectiontime;
	private Integer hostId;
}
