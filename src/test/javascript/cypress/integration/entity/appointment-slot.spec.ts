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

describe('AppointmentSlot e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load AppointmentSlots', () => {
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('AppointmentSlot').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details AppointmentSlot page', () => {
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('appointmentSlot');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create AppointmentSlot page', () => {
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AppointmentSlot');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit AppointmentSlot page', () => {
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('AppointmentSlot');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of AppointmentSlot', () => {
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AppointmentSlot');

    cy.get(`[data-cy="timeOfAppointment"]`).type('2021-03-23T16:27').invoke('val').should('equal', '2021-03-23T16:27');

    cy.get(`[data-cy="available"]`).should('not.be.checked');
    cy.get(`[data-cy="available"]`).click().should('be.checked');

    cy.get(`[data-cy="maxAppointments"]`).type('21').should('have.value', '21');

    cy.setFieldSelectToLastOfEntity('appointment');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of AppointmentSlot', () => {
    cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/appointment-slots/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment-slot');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('appointmentSlot').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/appointment-slots*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('appointment-slot');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
