#version 400 core

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;


in vec2 fragTexCoord;
in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColour;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight {
    vec3 colour;
    vec3 direction;
    float intensity;
};

struct PointLight {
    vec3 colour;
    vec3 position;
    float intensity;
    float constant;
    float linear;
    float exponent;
};

struct SpotLight {
    PointLight pl;
    vec3 conedir;
    float cutoff;
};


uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColours(Material material, vec2 textCoord) {
    if(material.hasTexture == 1) {
    ambientC = texture(textureSampler, textCoord);
    diffuseC = ambientC;
    specularC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
    vec4 diffuseColour = vec4(0,0,0,0);
    vec4 specularColour = vec4(0,0,0,0);

    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = diffuseC * vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

   vec3 camera_direction = normalize(-position);
   vec3 from_light_dir = -to_light_dir;
   vec3 reflectedLight = normalize(reflect(from_light_dir, normal));
   float specularFactor = max(dot(camera_direction, reflectedLight), 0.0);
   specularFactor = pow(specularFactor, specularPower);
   specularColour = specularC * light_intensity * specularFactor * material.reflectance * vec4(light_colour, 1.0);

   return (diffuseColour + specularColour);

}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec3 light_dir = light.position - position;
    vec3 to_light_dir = normalize(light_dir);
    vec4 light_colour = calcLightColour(light.colour, light.intensity, position, to_light_dir, normal);

    float distance = length(light_dir);
    float attenuationInv = light.constant + light.linear * distance + light.exponent * distance * distance;
    return light_colour / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal) {
    vec3 light_dir = light.pl.position - position;
    vec3 to_light_dir = normalize(light_dir);
    vec3 from_light_dir = -to_light_dir;
    float spot_alfa = dot(from_light_dir, normalize(light.conedir));

    vec4 colour = vec4(0, 0, 0, 0);

    if(spot_alfa > light.cutoff) {
    colour = calcPointLight(light.pl, position, normal);
    colour *= (1.0 - (1.0 - spot_alfa) / (1.0 - light.cutoff));
}
    return colour;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColour(light.colour, light.intensity, position, normalize(light.direction), normal);
}

void main() {

    setupColours(material, fragTexCoord);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, fragPos, fragNormal);

    for(int i = 0; i < MAX_POINT_LIGHTS; i++){
        if(pointLights[i].intensity > 0) {
        diffuseSpecularComp += calcPointLight(pointLights[i], fragPos, fragNormal);
        }
    }

     for(int i = 0; i < MAX_SPOT_LIGHTS; i++){
            if(spotLights[i].pl.intensity > 0) {
            diffuseSpecularComp += calcSpotLight(spotLights[i], fragPos, fragNormal);
            }
        }





    fragColour = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp ;
}