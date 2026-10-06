"""Migrate the upstream 2.0.11 resources, without altering original assets."""
from pathlib import Path
import json, shutil
root=Path(__file__).resolve().parent
repo=root.parent
out=root/'src/main/resources'
def write(path,value):
 p=out/path;p.parent.mkdir(parents=True,exist_ok=True)
 p.write_text(json.dumps(value,indent=2)+'\n',encoding='utf-8')
for source in (repo/'common/src/main/resources',repo/'common/src/generated/resources',repo/'fabric/src/main/resources'):
 for p in source.rglob('*'):
  if p.is_file() and '.cache' not in p.parts:
   rel=p.relative_to(source)
   if str(rel).startswith('META-INF') and 'services' not in rel.parts:continue
   if p.name in ('fabric.mod.json','nirvana.accesswidener','pack.mcmeta'):continue
   if p.suffix in ('.accesswidener','.cfg'):continue
   dest=out/rel;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(p,dest)
# Preserve all recipe ingredients using 26.1's compact ingredient codec.
def migrate(value):
 if isinstance(value,list):return [migrate(v) for v in value]
 if isinstance(value,dict):
  if set(value)=={'item'}:return value['item']
  if set(value)=={'tag'}:return '#'+value['tag']
  return {k:migrate(v) for k,v in value.items()}
 return value
for p in (out/'data').rglob('*.json'):
 if 'recipe' not in p.parts:continue
 value=json.loads(p.read_text());write(p.relative_to(out),migrate(value))
for p in (out/'META-INF/services').glob('*'):
 if 'IDataGenHelper' in p.name or 'IClientPlatformHelper' in p.name:p.unlink()
write(Path('fabric.mod.json'),{
 'schemaVersion':1,'id':'nirvana','version':'${version}','name':'Nirvana',
 'description':'Unofficial Minecraft 26.1.1 Fabric source port of Team Galena Nirvana 2.0.11.',
 'authors':['possible_triangle','Team Galena'],'contributors':['aidanpink (26.1.1 port)'],
 'contact':{'sources':'https://github.com/aidanpink/Nirvana/tree/port/26.1.1-fabric','issues':'https://github.com/aidanpink/Nirvana/issues'},
 'license':'MIT (code); All Rights Reserved (upstream assets)', 'environment':'*',
 'accessWidener':'nirvana.accesswidener',
 'entrypoints':{'main':['galena.nirvana.fabric.FabricEntrypoint'],'client':['galena.nirvana.fabric.client.FabricClientEntrypoint'],'jei_mod_plugin':['galena.nirvana.fabric.compat.NirvanaJeiFabricPlugin']},
 'mixins':['nirvana.mixins.json','nirvana.fabric.mixins.json'],
 'depends':{'fabricloader':'>=0.19.5','fabric-api':'>=0.145.4','minecraft':'26.1.1','java':'>=25','forgeconfigapiport':'>=26.1.3'}
})
(out/'nirvana.accesswidener').write_text('accessWidener v2 official\nextendable method net/minecraft/world/entity/item/PrimedTnt explode ()V\n')
for name in ('nirvana.mixins.json','nirvana.fabric.mixins.json'):
 p=out/name;j=json.loads(p.read_text());j['compatibilityLevel']='JAVA_25'
 if name=='nirvana.mixins.json':
  j['mixins'].remove('CatMixin');j['client'].remove('PlayerItemInHandLayerAccessor');j['client'].append('CatMixin')
 write(Path(name),j)
# Every item model now needs an item definition. Model geometry and textures stay
# identical; the new selector replaces the old hand-written item renderer.
for p in (out/'assets/nirvana/models/item').glob('*.json'):
 write(Path('assets/nirvana/items')/p.name,{'model':{'type':'minecraft:model','model':'nirvana:item/'+p.stem}})
for name,contexts in [('joint',['thirdperson_lefthand','thirdperson_righthand','head']),('deerstalker',['head'])]:
 write(Path(f'assets/nirvana/items/{name}.json'),{'model':{'type':'minecraft:select','property':'minecraft:display_context','cases':[{'when':contexts,'model':{'type':'minecraft:model','model':f'nirvana:item/{name}_equipped'}}],'fallback':{'type':'minecraft:model','model':f'nirvana:item/{name}_flat'}}})
write(Path('assets/nirvana/items/potion_bong.json'),{'model':{'type':'minecraft:model','model':'nirvana:item/potion_bong','tints':[{'type':'minecraft:constant','value':-1},{'type':'minecraft:potion','default':-13083194}]}})
write(Path('assets/nirvana/models/item/reefer_spawn_egg.json'),{'parent':'minecraft:item/generated','textures':{'layer0':'nirvana:item/spawn_egg','layer1':'nirvana:item/spawn_egg_overlay'}})
write(Path('assets/nirvana/items/reefer_spawn_egg.json'),{'model':{'type':'minecraft:model','model':'nirvana:item/reefer_spawn_egg','tints':[{'type':'minecraft:constant','value':0x619932},{'type':'minecraft:constant','value':0x2f4f15}]}})
write(Path('assets/nirvana/items/reefer_head.json'),{'model':{'type':'minecraft:special','base':'minecraft:item/template_skull','model':{'type':'minecraft:head','kind':'nirvana:reefer'}}})
# Vanilla trade pools use additive tags in 26.1. Keep prices, levels, counts,
# max uses, experience, and the random choice of burlap from the original code.
for level,item,count in [(2,'hemp_cloth',4),(3,'hemp_burlap',4),(4,'deerstalker',1)]:
 key=f'leatherworker/{level}/{item}'
 trade={'wants':{'id':'minecraft:emerald','count':10},'gives':{'id':f'nirvana:{item}','count':count},'max_uses':12,'xp':30,'reputation_discount':0.05}
 if level==3:trade['given_item_modifiers']=[{'function':'nirvana:random_burlap'}]
 write(Path(f'data/nirvana/villager_trade/{key}.json'),trade)
 write(Path(f'data/minecraft/tags/villager_trade/leatherworker/level_{level}.json'),{'replace':False,'values':['nirvana:'+key]})
# Preserve the phosphor feedback buffer across frames explicitly.
def post(fragment,source,target,extra=None,uniforms=None):
 p={'vertex_shader':'minecraft:core/screenquad','fragment_shader':fragment,'inputs':[{'sampler_name':'In','target':source}], 'output':target}
 if extra:p['inputs']+=extra
 if uniforms:p['uniforms']=uniforms
 return p
blit={'BlitConfig':[{'name':'ColorModulate','type':'vec4','value':[1,1,1,1]}]}
write(Path('assets/nirvana/post_effect/peace.json'),{
 'targets':{'swap':{},'saturated':{},'previous':{'persistent':True}},
 'passes':[
  post('nirvana:post/saturation','minecraft:main','saturated'),
  post('nirvana:post/phosphor','saturated','swap',[{'sampler_name':'Prev','target':'previous'}]),
  post('minecraft:post/blit','swap','previous',uniforms=blit),
  post('minecraft:post/blit','swap','minecraft:main',uniforms=blit)]})
shader=out/'assets/nirvana/shaders/post';shader.mkdir(parents=True,exist_ok=True)
(shader/'phosphor.fsh').write_text('''#version 330
uniform sampler2D InSampler;
uniform sampler2D PrevSampler;
in vec2 texCoord;
out vec4 fragColor;
void main() { fragColor = vec4(max(texture(PrevSampler,texCoord).rgb * vec3(0.8,0.85,0.8), texture(InSampler,texCoord).rgb),1.0); }
''')
(shader/'saturation.fsh').write_text('''#version 330
uniform sampler2D InSampler;
in vec2 texCoord;
out vec4 fragColor;
void main() { vec4 c=texture(InSampler,texCoord); float l=dot(c.rgb,vec3(0.3,0.59,0.11)); fragColor=vec4(mix(vec3(l),c.rgb,1.4),c.a); }
''')
feature=Path('data/nirvana/worldgen/configured_feature/patch_wild_hemp.json')
value=json.loads((out/feature).read_text());value['type']='nirvana:wild_hemp_patch';write(feature,value)
for name,texture in [('pipe_in_hand','nirvana:item/old_pipe'),('deerstalker_equipped','nirvana:item/deerstalker_equipped')]:
 path=Path(f'assets/nirvana/models/item/{name}.json');value=json.loads((out/path).read_text())
 value.setdefault('textures',{})['particle']=texture;write(path,value)
print('Migrated',len(list(out.rglob('*'))),'resource paths')

# Normalize legacy tag directory names still present in upstream hand-written data.
for namespace in (out/'data').iterdir():
 tags=namespace/'tags'
 for old,new in [('items','item'),('blocks','block'),('entity_types','entity_type')]:
  if (tags/old).exists():
   for p in (tags/old).rglob('*.json'):
    write((tags/new/p.relative_to(tags/old)).relative_to(out),json.loads(p.read_text()))
