import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('AppointmentSettings e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load AppointmentSettings', () => {
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('AppointmentSettings').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details AppointmentSettings page', () => {
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('appointmentSettings');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create AppointmentSettings page', () => {
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AppointmentSettings');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit AppointmentSettings page', () => {
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('AppointmentSettings');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of AppointmentSettings', () => {
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AppointmentSettings');

    cy.get(`[data-cy="maxOrdinaryAppointments"]`).type('500').should('have.value', '500');

    cy.get(`[data-cy="maxEmergencyAppointments"]`).type('715').should('have.value', '715');

    cy.get(`[data-cy="startingWorkTime"]`).type('4').should('have.value', '4');

    cy.get(`[data-cy="endingWorkTime"]`).type('15').should('have.value', '15');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of AppointmentSettings', () => {
    cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/appointment-settings/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-settings');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('appointmentSettings').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/appointment-settings*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('appointment-settings');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
