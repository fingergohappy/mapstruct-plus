import{b as C,B as L,y as I}from"./styles-7ff74f5d-254d53bb.js";import{M as c,$ as y,X as T,h as M,e as $,T as m,d as w,I as O,K as N}from"./mermaid.esm.min-92bda8ab.js";import{A as R}from"./index-7f531408-29aeeba9.js";import{h as j}from"./index-88b1ee15-dc8cd431.js";import"./app-98a7438e.js";import"./framework-2d755bf3.js";import"./isPlainObject-0b348591-e80b07b5.js";import"./array-2ff2c7a6-ffeda358.js";import"./constant-2fe7eae5-45b4460e.js";import"./edges-69864166-4bd01f28.js";import"./svgDraw-1c03c25e-ab425d9a.js";const D=o=>N.sanitizeText(o,y());let A={dividerMargin:10,padding:5,textHeight:10};const J=function(o,a,g,s){const t=Object.keys(o);c.info("keys:",t),c.info(o),t.forEach(function(l){const e=o[l];let d="";e.cssClasses.length>0&&(d=d+" "+e.cssClasses.join(" "));const i={labelStyle:""};let b=e.text!==void 0?e.text:e.id,r=0,p="";switch(e.type){case"class":p="class_box";break;default:p="class_box"}a.setNode(e.id,{labelStyle:i.labelStyle,shape:p,labelText:D(b),classData:e,rx:r,ry:r,class:d,style:i.style,id:e.id,domId:e.domId,tooltip:s.db.getTooltip(e.id)||"",haveCallback:e.haveCallback,link:e.link,width:e.type==="group"?500:void 0,type:e.type,padding:y().flowchart.padding}),c.info("setNode",{labelStyle:i.labelStyle,shape:p,labelText:b,rx:r,ry:r,class:d,style:i.style,id:e.id,width:e.type==="group"?500:void 0,type:e.type,padding:y().flowchart.padding})})},_=function(o,a,g,s){c.info(o),o.forEach(function(t,l){const e=t;let d="";const i={labelStyle:"",style:""};let b=e.text,r=0,p="note";if(a.setNode(e.id,{labelStyle:i.labelStyle,shape:p,labelText:D(b),noteData:e,rx:r,ry:r,class:d,style:i.style,id:e.id,domId:e.id,tooltip:"",type:"note",padding:y().flowchart.padding}),c.info("setNode",{labelStyle:i.labelStyle,shape:p,labelText:b,rx:r,ry:r,style:i.style,id:e.id,type:"note",padding:y().flowchart.padding}),!e.class||!(e.class in s))return;const f=g+l,n={};n.classes="relation",n.pattern="dotted",n.id=`edgeNote${f}`,n.arrowhead="none",c.info(`Note edge: ${JSON.stringify(n)}, ${JSON.stringify(e)}`),n.startLabelRight="",n.endLabelLeft="",n.arrowTypeStart="none",n.arrowTypeEnd="none";let u="fill:none",x="";n.style=u,n.labelStyle=x,n.curve=m(A.curve,w),a.setEdge(e.id,e.class,n,f)})},q=function(o,a){const g=y().flowchart;let s=0;o.forEach(function(t){s++;const l={};l.classes="relation",l.pattern=t.relation.lineType==1?"dashed":"solid",l.id="id"+s,t.type==="arrow_open"?l.arrowhead="none":l.arrowhead="normal",c.info(l,t),l.startLabelRight=t.relationTitle1==="none"?"":t.relationTitle1,l.endLabelLeft=t.relationTitle2==="none"?"":t.relationTitle2,l.arrowTypeStart=E(t.relation.type1),l.arrowTypeEnd=E(t.relation.type2);let e="",d="";if(t.style!==void 0){const i=O(t.style);e=i.style,d=i.labelStyle}else e="fill:none";l.style=e,l.labelStyle=d,t.interpolate!==void 0?l.curve=m(t.interpolate,w):o.defaultInterpolate!==void 0?l.curve=m(o.defaultInterpolate,w):l.curve=m(g.curve,w),t.text=t.title,t.text===void 0?t.style!==void 0&&(l.arrowheadStyle="fill: #333"):(l.arrowheadStyle="fill: #333",l.labelpos="c",y().flowchart.htmlLabels?(l.labelType="html",l.label='<span class="edgeLabel">'+t.text+"</span>"):(l.labelType="text",l.label=t.text.replace(N.lineBreakRegex,`
`),t.style===void 0&&(l.style=l.style||"stroke: #333; stroke-width: 1.5px;fill:none"),l.labelStyle=l.labelStyle.replace("color:","fill:"))),a.setEdge(t.id1,t.id2,l,s)})},z=function(o){Object.keys(o).forEach(function(a){A[a]=o[a]})},H=function(o,a,g,s){c.info("Drawing class - ",a);const t=y().flowchart,l=y().securityLevel;c.info("config:",t);const e=t.nodeSpacing||50,d=t.rankSpacing||50,i=new R({multigraph:!0,compound:!0}).setGraph({rankdir:s.db.getDirection(),nodesep:e,ranksep:d,marginx:8,marginy:8}).setDefaultEdgeLabel(function(){return{}}),b=s.db.getClasses(),r=s.db.getRelations(),p=s.db.getNotes();c.info(r),J(b,i,a,s),q(r,i),_(p,i,r.length+1,b);let f;l==="sandbox"&&(f=T("#i"+a));const n=l==="sandbox"?T(f.nodes()[0].contentDocument.body):T("body"),u=n.select(`[id="${a}"]`),x=n.select("#"+a+" g");if(j(x,i,["aggregation","extension","composition","dependency","lollipop"],"classDiagram",a),M.insertTitle(u,"classTitleText",t.titleTopMargin,s.db.getDiagramTitle()),$(i,u,t.diagramPadding,t.useMaxWidth),!t.htmlLabels){const k=l==="sandbox"?f.nodes()[0].contentDocument:document,B=k.querySelectorAll('[id="'+a+'"] .edgeLabel .label');for(const S of B){const v=S.getBBox(),h=k.createElementNS("http://www.w3.org/2000/svg","rect");h.setAttribute("rx",0),h.setAttribute("ry",0),h.setAttribute("width",v.width),h.setAttribute("height",v.height),S.insertBefore(h,S.firstChild)}}};function E(o){let a;switch(o){case 0:a="aggregation";break;case 1:a="extension";break;case 2:a="composition";break;case 3:a="dependency";break;case 4:a="lollipop";break;default:a="none"}return a}const K={setConf:z,draw:H},te={parser:C,db:L,renderer:K,styles:I,init:o=>{o.class||(o.class={}),o.class.arrowMarkerAbsolute=o.arrowMarkerAbsolute,L.clear()}};export{te as diagram};
